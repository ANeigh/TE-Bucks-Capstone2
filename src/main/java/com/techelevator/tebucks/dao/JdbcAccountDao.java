package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.Transfer;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountById(long id) {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            return mapRowToAccount(results);
        } else {
            return null;
        }
    }

    @Override
    public boolean update(Transfer transfer) {
        boolean fromUserSuccess;
        boolean toUserSuccess;
        BigDecimal fromUserBalance = getBalance(transfer.getUserFrom().getId()).subtract(transfer.getAmount());
        BigDecimal toUserBalance = getBalance(transfer.getUserTo().getId()).add(transfer.getAmount());
        String sql = "UPDATE account SET balance = ? WHERE id = ? ";
        String sql2 = "UPDATE account SET balance = ? WHERE id = ? ";
        fromUserSuccess = jdbcTemplate.update(sql, fromUserBalance, transfer.getUserFrom().getId()) == 1;
        if (fromUserSuccess) {
            toUserSuccess = jdbcTemplate.update(sql2, toUserBalance, transfer.getUserTo().getId()) == 1;
        } else toUserSuccess = false;

        return (toUserSuccess);
    }

    @Override
    public BigDecimal getBalance(long id) {
        String sql = "Select balance FROM accounts WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            return mapRowToAccount(results).getBalance();
        }
        return null;
    }

    @Override
    public boolean createAccount(long userId) {
        String sql = "INSERT into accounts (account_id, user_id, balance, active)" +
                " VALUES (DEFAULT, ?, DEFAULT, DEFAULT) RETURNING account_id";
        Integer newAccountId;
        newAccountId =  jdbcTemplate.queryForObject(sql,Integer.class, userId);
        return newAccountId != null;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setId(rs.getInt("account_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setActivated(rs.getBoolean("active"));
        return account;
    }

}
