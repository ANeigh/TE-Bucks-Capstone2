package com.techelevator.tebucks.model;

import java.math.BigDecimal;

public class Account {
    private int accountId;
    private int userId;
    private BigDecimal balance;
    private boolean activated;

    public Account() {}

    public Account(int accountId, int userId, BigDecimal balance, boolean activated) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
        this.activated = activated;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setId(int id) {
        this.accountId = id;
    }

    public void setBalance(BigDecimal newBalance) {
        this.balance = newBalance;
    }

}
