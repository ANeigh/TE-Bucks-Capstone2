package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer getTransferById(long id);

    String getTransferStatus(long id);

    List<Transfer> getListOfTransfers(int userId);

    boolean createTransfer(Transfer newTransfer);

    boolean updateTransferStatus(Transfer newTransfer);
}
