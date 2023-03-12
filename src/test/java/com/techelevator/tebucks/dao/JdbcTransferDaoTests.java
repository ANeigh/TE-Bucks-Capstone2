package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.User;
import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcTransferDaoTests extends BaseDaoTests {
    protected static final User USER_1 = new User(1, "user1", "user1", "USER");
    protected static final User USER_2 = new User(2, "user2", "user2", "USER");
    private static final User USER_3 = new User(3, "user3", "user3", "USER");

    private static final BigDecimal bigDecimal = new BigDecimal("100.00");
    private static final BigDecimal bigDecimal2 = new BigDecimal("50.00");
    private static final BigDecimal bigDecimal3 = new BigDecimal("200.00");


    protected static final Transfer TRANSFER_1 = new Transfer(1, "Send", "Approved", USER_1, USER_2, bigDecimal);
    protected static final Transfer TRANSFER_2 = new Transfer(2, "Request", "Pending", USER_3, USER_2, bigDecimal2);
    protected static final Transfer TRANSFER_3 = new Transfer(3, "Request", "Rejected", USER_2, USER_1, bigDecimal3);
    protected static final Transfer TRANSFER_4 = new Transfer(4, "Request", "Approved", USER_3, USER_1, bigDecimal3);


    private JdbcUserDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        AccountDao accountDao = new JdbcAccountDao(jdbcTemplate);
        sut = new JdbcUserDao(jdbcTemplate, accountDao);
    }



}
