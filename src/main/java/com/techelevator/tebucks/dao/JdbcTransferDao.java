package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

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
    public Transfer getTransferById(long id) {

        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            return mapRowToTransfer(results);
        } else {
            return null;
        }
    }

    @Override
    public String getTransferStatus(long id) {
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
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public long createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_id, transfer_type, status, user_from, user_to, amount" +
                " VALUES (DEFAULT, ?, ?, ?, ?, ?) RETURNS transfer_id";

         return jdbcTemplate.update(sql, transfer.getTransferType(), transfer.getTransferStatus(), transfer.getUserFrom(),
                transfer.getUserTo(), transfer.getAmount());
    }

    @Override
    public boolean updateTransferStatus(Transfer newTransfer) {
        String sql = "UPDATE transfer SET status ? WHERE transfer_id = ? ";
        return jdbcTemplate.update(sql, newTransfer.getTransferStatus(), newTransfer.getTransferId()) == 1;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setAmount(rs.getBigDecimal("amount"));
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferStatus(rs.getString(""));
        transfer.setTransferType(rs.getString("transfer_type"));
        transfer.setUserFrom(userDao.findByUsername(rs.getString("user_from")));
        transfer.setUserTo(userDao.findByUsername(rs.getString("user_to")));
        return transfer;
    }

}
