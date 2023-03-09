package com.techelevator.tebucks.controller;

import com.techelevator.tebucks.dao.AccountDao;
import com.techelevator.tebucks.dao.TransferDao;
import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/api/transfers/")
public class TransferController {

    private final TransferDao transferDao;
    private final AccountDao accountDao;

    public TransferController(TransferDao transferDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    @GetMapping(path = "{id}")
    public Transfer getTransferByID(@PathVariable long id) {
        return transferDao.getTransferById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public boolean createTransfer(@RequestBody Transfer transfer) {
        if (accountDao.getBalance(transfer.getUserFrom().getId())
        return transferDao.createTransfer(transfer);
    }

    @PutMapping(path = "/{id}/status")
    public boolean updateTransferStatus(@RequestBody Transfer transfer, @PathVariable long transferId) {
        return transferDao.updateTransferStatus(transfer, transferId);
    }
}
