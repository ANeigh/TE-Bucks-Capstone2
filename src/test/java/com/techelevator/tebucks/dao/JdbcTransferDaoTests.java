package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests {
    protected static final User USER_1 = new User(1, "user1", "user1", "USER");
    protected static final User USER_2 = new User(2, "user2", "user2", "USER");
    private static final User USER_3 = new User(3, "user3", "user3", "USER");

    private static final BigDecimal bigDecimal = new BigDecimal("100.00");
    private static final BigDecimal bigDecimal2 = new BigDecimal("50.00");
    private static final BigDecimal bigDecimal3 = new BigDecimal("200.00");


    private static final Transfer TRANSFER_1 = new Transfer(1, "Send", "Approved", USER_1, USER_2, bigDecimal);
    private static final Transfer TRANSFER_2 = new Transfer(2, "Request", "Pending", USER_3, USER_2, bigDecimal2);
    private static final Transfer TRANSFER_3 = new Transfer(3, "Request", "Rejected", USER_2, USER_1, bigDecimal3);
    private static final Transfer TRANSFER_4 = new Transfer(4, "Request", "Approved", USER_3, USER_1, bigDecimal3);


    private JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        AccountDao accountDao = new JdbcAccountDao(jdbcTemplate);
        UserDao userDao = new JdbcUserDao(jdbcTemplate, accountDao);
        sut = new JdbcTransferDao(jdbcTemplate, userDao);
    }

    @Test
    public void getTransferbyId_given_invalid_transfer_id_returns_null() {
        Transfer transfer = sut.getTransferById(-1);

        Assert.assertNull(transfer);
    }

    //user objects different, will never pass(?)
    @Test
    public void geTransferById_given_valid_transfer_id_returns_transfer() {
        Transfer transfer = sut.getTransferById((int)TRANSFER_1.getTransferId());

        Assert.assertEquals(TRANSFER_1, transfer);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getTransferStatus_given_invalid_transfer_id_does_throw_exception() {
        String status = sut.getTransferStatus(-1);

    }

    @Test
    public void getTransferStatus_returns_correct_status (){
        String status = sut.getTransferStatus(1);

        Assert.assertEquals(TRANSFER_1.getTransferStatus(), status);
    }

    @Test
    public void getListOfTransfers_returns_all_transfers() {
        List<Transfer> transfers = sut.getListOfTransfers(2);

        Assert.assertNotNull(transfers);
        Assert.assertEquals(3, transfers.size());
    }
    //wont work because of User objects not being the same.
    @Test
    public void create_transfer_creates_a_transfer() {
        Transfer transfer = new Transfer(123, "Send", "Approved", USER_3, USER_1, bigDecimal);

        int transferWasCreated = sut.createTransfer(transfer);

        Transfer newTransfer = sut.getTransferById(transferWasCreated);

        Assert.assertEquals(transfer, newTransfer);
    }

    @Test
    public void updateTransferStatus_correctly_updates() {
        Transfer transfer = new Transfer(232,"Request", "Pending", USER_3, USER_1, bigDecimal);
        sut.createTransfer(transfer);
        Transfer newTransfer = new Transfer(232,"Request", "Rejected", USER_3, USER_1, bigDecimal);

        sut.updateTransferStatus(newTransfer);

        Assert.assertEquals("Rejected", newTransfer.getTransferStatus());

    }


}
