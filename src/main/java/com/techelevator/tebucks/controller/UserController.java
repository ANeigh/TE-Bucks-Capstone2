package com.techelevator.tebucks.controller;

import com.techelevator.tebucks.dao.JdbcTransferDao;
import com.techelevator.tebucks.dao.TransferDao;
import com.techelevator.tebucks.dao.UserDao;
import com.techelevator.tebucks.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.techelevator.tebucks.model.User;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/api/")
//@PreAuthorize("isAuthenticated()")
public class UserController {

    private final UserDao userDao;
    private final TransferDao transferDao;


public UserController(UserDao userDao, TransferDao transferDao) {
    this.userDao = userDao;
    this.transferDao = transferDao;
    }

@RequestMapping(path = "users", method = RequestMethod.GET)
    public List<User> getAllUsers(Principal principal) {
        List<User> users = userDao.findAll();
        for (User x : users) {
            if (x.getId() == userDao.findIdByUsername(principal.getName())) {
                users.remove(x);
            }
        }
        return users;
    }

@RequestMapping(path = "account/transfers", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(int userId) {
        return transferDao.getListOfTransfers(userId);
    }

}
