package com.techelevator.tebucks.controller;

import com.techelevator.tebucks.dao.JdbcTransferDao;
import com.techelevator.tebucks.dao.TransferDao;
import com.techelevator.tebucks.dao.UserDao;
import com.techelevator.tebucks.model.Transfer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.techelevator.tebucks.model.User;

import java.util.List;

@RestController
@RequestMapping(path = "/api/")
public class UserController {

    private final UserDao userDao;
    private final TransferDao transferDao;


public UserController(UserDao userDao, TransferDao transferDao) {
    this.userDao = userDao;
    this.transferDao = transferDao;

}

@RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return userDao.findAll();
}

@RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(int userId) {
        return transferDao.getListOfTransfers(userId);
}



}
