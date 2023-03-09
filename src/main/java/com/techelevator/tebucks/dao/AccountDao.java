package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    Account getAccountById(long id);

    boolean update(long id, Account account);

    BigDecimal getBalance(long id);

}
