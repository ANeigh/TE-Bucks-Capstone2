package com.techelevator.tebucks.controller;

import com.techelevator.tebucks.dao.AccountDao;
import com.techelevator.tebucks.dao.TransferDao;
import com.techelevator.tebucks.dao.UserDao;
import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.NewTransferDto;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.TransferStatusUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;

@RestController

@PreAuthorize("isAuthenticated()")
public class TransferController {

    private final TransferDao transferDao;
    private final AccountDao accountDao;
    private final UserDao userDao;

    public TransferController(TransferDao transferDao, AccountDao accountDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @GetMapping(path = "/api/transfers/{id}")
    public Transfer getTransferByID(@PathVariable Integer id) {
        return transferDao.getTransferById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/api/transfers")
    public Transfer createTransfer(@RequestBody NewTransferDto transferDto) {
        BigDecimal minAmount = new BigDecimal("0.01");
        Transfer transfer = new Transfer();
        transfer.setAmount(transferDto.getAmount());
        transfer.setTransferType(transferDto.getTransferType());
        transfer.setUserFrom(userDao.getUserById(transferDto.getUserFrom()));
        transfer.setUserTo(userDao.getUserById(transferDto.getUserTo()));


        transfer.setTransferStatus(Transfer.TRANSFER_STATUS_PENDING);

        if (transfer.isSendType()) {
            transfer.setTransferStatus(Transfer.TRANSFER_STATUS_APPROVED);
        }
        if (transfer.getAmount().compareTo(minAmount) >= 0) {
            if (accountDao.getBalance(transfer.getUserFrom().getId()).compareTo(transfer.getAmount()) >= 0) {
                if (transfer.isApproved()) {
                    accountDao.update(transfer);
                }
                transfer.setTransferId(transferDao.createTransfer(transfer));
                return transfer;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Insufficient Funds... broke ass");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Transfer amount must be greater than $0.00");
        }
    }

    @PutMapping(path = "/api/transfers/{id}/status")
    public Transfer updateTransferStatus(@RequestBody TransferStatusUpdateDto transferStatusUpdateDto, @PathVariable Integer transferId) {
        Transfer transfer = new Transfer();
        transfer.setTransferStatus(transferStatusUpdateDto.getTransferStatus());
        transfer.setTransferId(transferId);

        boolean updateSuccessful =  transferDao.updateTransferStatus(transfer);
        if (updateSuccessful && transfer.isApproved()) {
            accountDao.update(transfer);
        }
        return transfer;
    }

}
