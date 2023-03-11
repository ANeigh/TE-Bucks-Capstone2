package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.Transfer;

import java.math.BigDecimal;

public interface AccountDao {

    Account getAccountById(long id);

    boolean update(Transfer transfer);

    BigDecimal getBalance(long id);

    boolean createAccount(Integer userId);


}
