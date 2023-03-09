package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    Account getAccountById(long id);

    boolean update(long id, Account account);

<<<<<<< HEAD
    BigDecimal getBalance(long id);
=======
    BigDecimal getBalance(long Id);

    boolean createAccount(long userId);
>>>>>>> 0d097da51a472fd1d2dd8fc432a19f40a2d89c76

}
