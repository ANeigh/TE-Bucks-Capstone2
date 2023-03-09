package com.techelevator.tebucks.controller;

import com.techelevator.tebucks.dao.TransferDao;
import com.techelevator.tebucks.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/api/transfers/")
public class TransferController {

    private final TransferDao transferDao;

    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @GetMapping(path = "{id}")
    public Transfer getTransferByID(@PathVariable long id) {
        return transferDao.getTransferById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public boolean createTransfer(@RequestBody Transfer transfer) {
        return transferDao.createTransfer(transfer);
    }

    @PutMapping(path = "/{id}/status")
    public boolean updateTransferStatus(@RequestBody Transfer transfer, @PathVariable long transferId) {
        return transferDao.updateTransferStatus(transfer, transferId);
    }
}
