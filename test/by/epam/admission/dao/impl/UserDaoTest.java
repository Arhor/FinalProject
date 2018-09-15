/*
 * class: UserDaoTest
 */

package by.epam.admission.dao.impl;

import by.epam.admission.dao.TransactionHelper;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.User;
import by.epam.admission.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.*;

/**
 * @author Maxim Burishinets
 * @version 1.0 3 Sep 2018
 */
public class UserDaoTest {

    private static final Logger LOG = LogManager.getLogger(UserDaoTest.class);

    @Test
    public void testFindAll() {
        TransactionHelper t = new TransactionHelper();
        UserDao uDAO = new UserDao();
        t.startTransaction(uDAO);
        try {
            for (User user : uDAO.findAll()) {
                LOG.info(user);
            }
        } catch (ProjectException e) {
            LOG.error(e);
        }
        t.endTransaction();
    }

    @Test
    public void testFindEntityById() {
        for (int i = 0; i < 100; i++) {
            new Thread() {
                public void run() {
                    TransactionHelper t = new TransactionHelper();
                    UserDao uDAO = new UserDao();
                    t.startTransaction(uDAO);
                    int id = (int)(Math.random() * 55 + 0.5);
                    try {
                        LOG.info("UID: " + id + " - " + uDAO.findEntityById(id));
                    } catch (ProjectException e) {
                        LOG.error(e);
                    }
                    t.endTransaction();
                }
            }.start();
        }
    }

    @Test
    public void testFindUserByEmailAndPassword() {
        String email = "example.30@gmail.com";
        String password = "example.30@gmail.com";
        TransactionHelper t = new TransactionHelper();
        UserDao uDAO = new UserDao();
        t.startTransaction(uDAO);
        try {
            LOG.info(uDAO.findUserByEmailAndPassword(email, password));
        } catch (ProjectException e) {
            LOG.error(e);
        }
        t.endTransaction();
    }

    @Test
    public void testDeleteByEmailAndPassword() {
        User user = new User();
        user.setEmail("test@gmail.com");
        String password = "test@gmail.com";
        TransactionHelper t = new TransactionHelper();
        UserDao uDAO = new UserDao();
        t.startTransaction(uDAO);
        try {
            LOG.info(uDAO.delete(user, password));
            t.commit();
        } catch (ProjectException e) {
            LOG.error("DAO exception", e);
            t.rollback();
        }
        t.endTransaction();
    }

    @Test
    public void testMultiCreate() {
        for (int i = 1; i < 11; i++) {
            final int number = i;
            new Thread() {
                public void run() {
                    TransactionHelper t = new TransactionHelper();
                    UserDao uDAO = new UserDao();
                    t.startTransaction(uDAO);
                    User user = new User();
                    user.setEmail("test"+ number +"@gmail.com");
                    user.setFirstName("test" + number);
                    user.setLastName("test" + number);
                    user.setRole(User.Role.CLIENT);
                    user.setLang(User.Lang.RU);
                    String password = "test"+ number +"@gmail.com";
                    try {
                        uDAO.create(user, password);
                        t.commit();
                        LOG.info("created user: " + user);
                    } catch (ProjectException e) {
                        LOG.error("DAO exception", e);
                        t.rollback();
                    }
                    t.endTransaction();
                }
            }.start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOG.error("Interrupted exception", e);
            Thread.currentThread().interrupt();
        }
    }

    @Test
    public void testCreate() {
        TransactionHelper t = new TransactionHelper();
        UserDao uDAO = new UserDao();
        t.startTransaction(uDAO);
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setFirstName("test");
        user.setLastName("test");
        user.setRole(User.Role.CLIENT);
        user.setLang(User.Lang.RU);
        String password = "test@gmail.com";
        try {
            boolean flag = uDAO.create(user, password);
            t.commit();
            if (flag) {
                LOG.info("created user: " + user);
            }
        } catch (ProjectException e) {
            LOG.error("DAO exception", e);
            t.rollback();
        }
        t.endTransaction();
    }

    @Test
    public void testUpdateByEmailAndPassword() {
        TransactionHelper t = new TransactionHelper();
        UserDao uDAO = new UserDao();
        t.startTransaction(uDAO);
        User user = new User();
        user.setEmail("example.48@gmail.com");
        user.setFirstName("xxx");
        user.setLastName("xxx");
        user.setLang(User.Lang.RU);
        String password = "example.48@gmail.com";
        User result = null;
        try {
            result = uDAO.update(user, password);
            t.commit();
        } catch (ProjectException e) {
            t.rollback();
        }
        LOG.debug(user);
        Assert.assertNotNull(result);
    }

    @Test
    public void testDelete() {
        boolean result = false;
        TransactionHelper t = new TransactionHelper();
        UserDao uDAO = new UserDao();
        t.startTransaction(uDAO);
        try {
            result = uDAO.delete(50);
            t.commit();
        } catch (ProjectException e) {
            t.rollback();
        }
        Assert.assertFalse(result);
    }

    @BeforeClass
    public void setUp() {
    }

    @AfterClass
    public void tearDown() {
        ConnectionPool.POOL.closePool();
        LOG.info("available connections: "
                + ConnectionPool.POOL.countAvailableConnections());
        LOG.info("used connections: "
                + ConnectionPool.POOL.countUsedConnections());
    }

}