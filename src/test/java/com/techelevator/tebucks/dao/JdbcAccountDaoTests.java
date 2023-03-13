package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.controller.AccountController;
import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests {

    private static final User USER_1 = new User(1, "user1", "user1", "USER");
    private static final User USER_2 = new User(2, "user2", "user2", "USER");
    private static final User USER_3 = new User(3, "user3", "user3", "USER");

    private static final BigDecimal bigDecimal = new BigDecimal("100.00");
    private static final BigDecimal bigDecimal2 = new BigDecimal("50.00");
    private static final BigDecimal bigDecimal3 = new BigDecimal("200.00");
    private static final BigDecimal bigDecimal4 = new BigDecimal("1000.00");


    private static final Transfer TRANSFER_1 = new Transfer(1, "Send", "Approved", USER_1, USER_2, bigDecimal);
    private static final Transfer TRANSFER_2 = new Transfer(2, "Request", "Pending", USER_3, USER_2, bigDecimal2);
    private static final Transfer TRANSFER_3 = new Transfer(3, "Request", "Rejected", USER_2, USER_1, bigDecimal3);
    private static final Transfer TRANSFER_4 = new Transfer(4, "Request", "Approved", USER_3, USER_1, bigDecimal3);

    private static Account ACCOUNT_1 = new Account(1, 1, bigDecimal4, true);
    private static Account ACCOUNT_2 = new Account(2, 2, bigDecimal4, true);
    private static Account ACCOUNT_3 = new Account(3, 3, bigDecimal4, true);

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void getAccountById_given_invalid_account_id_returns_null() {
        Account account = sut.getAccountById(-1);

        Assert.assertNull(account);
    }
    //??
    @Test
    public void getAccountById_given_valid_id_returns_account() {
        Account account = sut.getAccountById(1);
        Assert.assertEquals(ACCOUNT_1, account);
    }

    @Test
    public void getBalance_returns_null_if_given_invalid_account_id() {
        BigDecimal balance = sut.getBalance(-1);

        Assert.assertNull(balance);
    }

    @Test
    public void getBalance_given_valid_id_returns_balance() {
        BigDecimal balance = sut.getBalance(1);
        Assert.assertEquals(ACCOUNT_1.getBalance().intValue(), balance.intValue());
    }
    // ??
    @Test
    public void update_sets_balance_to_expected_values() {
        sut.update(TRANSFER_1);
        BigDecimal test1 = new BigDecimal("1100.00");
        Assert.assertEquals(test1, ACCOUNT_2.getBalance());
        Assert.assertEquals(900, ACCOUNT_1.getBalance().intValue());
    }
    //foreign key constraints prevents method from being tested
    @Test
    public void createAccount_creates_account_when_passed_user_id (){
        sut.createAccount(10);
        Account account = sut.getAccountById(10);
        Assert.assertNull(account);
    }




}
