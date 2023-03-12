package com.techelevator.tebucks.model;

import java.math.BigDecimal;

public class TxLogDto {

    private String description;
    private String username_from;
    private String username_to;
    private BigDecimal amount;

    public String getDescription() {
        return description;
    }

    public String getUsername_from() {
        return username_from;
    }

    public String getUsername_to() {
        return username_to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUsername_from(String username_from) {
        this.username_from = username_from;
    }

    public void setUsername_to(String username_to) {
        this.username_to = username_to;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
