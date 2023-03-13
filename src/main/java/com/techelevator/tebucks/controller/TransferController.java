package com.techelevator.tebucks.controller;

import com.techelevator.tebucks.dao.AccountDao;
import com.techelevator.tebucks.dao.TransferDao;
import com.techelevator.tebucks.dao.UserDao;
import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.NewTransferDto;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.TransferStatusUpdateDto;
import com.techelevator.tebucks.services.TearsService;
import com.techelevator.tebucks.util.BasicLogger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.websocket.RemoteEndpoint;
import java.math.BigDecimal;
import java.security.Principal;

@RestController

@PreAuthorize("isAuthenticated()")
public class TransferController {

    private final TransferDao transferDao;
    private final AccountDao accountDao;
    private final UserDao userDao;
    private final TearsService tearsService = new TearsService();

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
        BigDecimal tearsThreshold = new BigDecimal("1000.00");
        Transfer transfer = new Transfer();
        transfer.setAmount(transferDto.getAmount());
        transfer.setTransferType(transferDto.getTransferType());
        transfer.setUserFrom(userDao.getUserById(transferDto.getUserFrom()));
        transfer.setUserTo(userDao.getUserById(transferDto.getUserTo()));

        if (transfer.getUserFrom().getId() == transfer.getUserTo().getId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer must be between two unique accounts.");
        }
        if (transfer.getAmount().compareTo(minAmount) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer amount must be greater than $0.00");
        }

        transfer.setTransferStatus(Transfer.TRANSFER_STATUS_PENDING);

        if (transfer.isSendType()) {
            transfer.setTransferStatus(Transfer.TRANSFER_STATUS_APPROVED);
            if (transfer.getAmount().compareTo(tearsThreshold) >= 0) {
                tearsService.reportTransferToTEARS(transfer, "The transfer amount is or exceeds $1000.00");
            }
            if (accountDao.getBalance(transfer.getUserFrom().getId()).compareTo(transfer.getAmount()) >= 0) {
                transferDao.createTransfer(transfer);
                accountDao.update(transfer);
            } else {
                tearsService.reportTransferToTEARS(transfer, "The user requested a transfer that would have" +
                        " overdrawn their account");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient Funds... broke ass");
            }
            return transfer;

        } else {
                transfer.setTransferId(transferDao.createTransfer(transfer));
                return transfer;
            }
    }
    
    @CrossOrigin(origins = "https://tebucks.netlify.app")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    @PutMapping(path = "/api/transfers/{id}/status")
    public Transfer updateTransferStatus(@RequestBody TransferStatusUpdateDto transferStatusUpdateDto, @PathVariable("id") Integer transferId) {
        BigDecimal tearsThreshold = new BigDecimal("1000.00");
        Transfer transfer = transferDao.getTransferById(transferId);
        transfer.setTransferStatus(transferStatusUpdateDto.getTransferStatus());

        if (transfer.isApproved()) {
            if (transfer.getAmount().compareTo(tearsThreshold) >= 0) {
                tearsService.reportTransferToTEARS(transfer, "The transfer amount is or exceeds $1000.00");
            }
            if (accountDao.getBalance(transfer.getUserFrom().getId()).compareTo(transfer.getAmount()) >= 0) {
                transferDao.updateTransferStatus(transfer);
                accountDao.update(transfer);
            } else {
                tearsService.reportTransferToTEARS(transfer, "The user requested a transfer that would have" +
                        " overdrawn their account");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient Funds... broke ass");
            }
        } else {
            transferDao.updateTransferStatus(transfer);
        }
        return transfer;
    }
}
