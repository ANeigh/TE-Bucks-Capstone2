package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
//        this.userDao = new JdbcUserDao(jdbcTemplate);
        this.userDao = userDao;
    }

    @Override
    public Transfer getTransferById(int id) {

        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            return mapRowToTransfer(results);
        } else {
            return null;
        }
    }

    @Override
    public String getTransferStatus(int id) {
        String sql = "SELECT status FROM transfer WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            return mapRowToTransfer(results).getTransferStatus();
        }
        return null;
    }

    @Override
    public List<Transfer> getListOfTransfers(int userId) {
        String username = userDao.getUserById(userId).getUsername();
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE user_to = ? or user_from = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username, username);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public int createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_id, transfer_type, status, user_from, user_to, amount) VALUES (DEFAULT, ?, ?, ?, ?, ?) RETURNING transfer_id";
        int transferId;
        try {
            transferId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTransferType(), transfer.getTransferStatus(), transfer.getUserFrom().getUsername(),
                    transfer.getUserTo().getUsername(), transfer.getAmount());
        } catch(DataAccessException | NullPointerException e) {
            throw new NullPointerException("Unable to create new transfer");
        }
        return transferId;
    }

    @Override
    public boolean updateTransferStatus(Transfer newTransfer) {
        String sql = "UPDATE transfer SET status = ? WHERE transfer_id = ? ";
        return jdbcTemplate.update(sql, newTransfer.getTransferStatus(), newTransfer.getTransferId()) == 1;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setAmount(rs.getBigDecimal("amount"));
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferStatus(rs.getString("status"));
        transfer.setTransferType(rs.getString("transfer_type"));
        transfer.setUserFrom(userDao.findByUsername(rs.getString("user_from")));
        transfer.setUserTo(userDao.findByUsername(rs.getString("user_to")));
        return transfer;
    }
}
