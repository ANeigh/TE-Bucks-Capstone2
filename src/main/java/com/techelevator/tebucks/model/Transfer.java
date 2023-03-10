package com.techelevator.tebucks.model;

import com.techelevator.tebucks.dao.JdbcUserDao;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;

import java.math.BigDecimal;

public class Transfer {
    private long transferId;
    private String transferType;
    private String transferStatus;
    private User userFrom;
    private User userTo;
    private BigDecimal amount;

    public static final String TRANSFER_TYPE_REQUEST = "Request";
    public static final String TRANSFER_TYPE_SEND = "Send";
    public static final String TRANSFER_STATUS_PENDING = "Pending";
    public static final String TRANSFER_STATUS_APPROVED = "Approved";
    public static final String TRANSFER_STATUS_REJECTED = "Rejected";


	public long getTransferId() {
        return transferId;
    }

    public String getTransferType() {
        return transferType;
    }

    public String getTransferStatus() {
        return transferStatus;
    }
    
    public User getUserFrom() {
    	return userFrom;
    }
    
    public User getUserTo() {
    	return userTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

	public boolean isApproved() {
        return transferStatus.equals(TRANSFER_STATUS_APPROVED);
    }
	
	public boolean isRejected() {
        return transferStatus.equals(TRANSFER_STATUS_REJECTED);
    }
	
	public boolean isPending() {
        return transferStatus.equals(TRANSFER_STATUS_PENDING);
    }
	
	public boolean isRequestType() {
        return transferType.equals(TRANSFER_TYPE_REQUEST);
    }
	
	public boolean isSendType() {
        return transferType.equals(TRANSFER_TYPE_SEND);
    }

    public void setTransferId(long transferId) {
        this.transferId = transferId;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
