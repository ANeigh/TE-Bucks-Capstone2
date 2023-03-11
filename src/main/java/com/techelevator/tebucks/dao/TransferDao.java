package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer getTransferById(int id);

    String getTransferStatus(int id);

    List<Transfer> getListOfTransfers(int userId);

    int createTransfer(Transfer newTransfer);

    boolean updateTransferStatus(Transfer newTransfer);
}
