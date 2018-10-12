/*
 * class: UserDaoTest
 */

package by.epam.admission.dao.impl;

import by.epam.admission.dao.DaoHelper;
import by.epam.admission.dao.DaoHelperDbUnit;
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
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        UserDao uDAO = new UserDao();
        try {
            daoHelper.startTransaction(uDAO);
            int totalUsers = uDAO.findTotalAmount();
            LOG.info("total users: " + totalUsers);
            for (User user : uDAO.findAll(0,totalUsers)) { // TODO: STUB
                LOG.info(user);
            }
        } catch (ProjectException e) {
            LOG.error(e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test
    public void testFindEntityById() {
        for (int i = 0; i < 100; i++) {
            new Thread() {
                public void run() {
                    DaoHelper daoHelper = new DaoHelper();
                    UserDao uDAO = new UserDao();
                    int id = (int)(Math.random() * 55 + 0.5);
                    try {
                        daoHelper.startTransaction(uDAO);
                        LOG.info("UID: " + id + " - " + uDAO.findEntityById(id));
                    } catch (ProjectException e) {
                        LOG.error(e);
                    } finally {
                        daoHelper.endTransaction();
                    }
                }
            }.start();
        }
    }

//    @Test
//    public void testFindUserByEmailAndPassword() {
//        String email = "example.30@gmail.com";
//        String password = "example.30@gmail.com";
//        DaoHelper daoHelper = new DaoHelper();
//        UserDao uDAO = new UserDao();
//        try {
//            daoHelper.startTransaction(uDAO);
//            LOG.info(uDAO.findUserByEmailAndPassword(email, password));
//        } catch (ProjectException e) {
//            LOG.error(e);
//        } finally {
//            daoHelper.endTransaction();
//        }
//    }

//    @Test
//    public void testDeleteByEmailAndPassword() {
//        User user = new User();
//        user.setEmail("test@gmail.com");
//        String password = "test@gmail.com";
//        DaoHelper daoHelper = new DaoHelper();
//        UserDao uDAO = new UserDao();
//        try {
//            daoHelper.startTransaction(uDAO);
//            LOG.info(uDAO.delete(user, password));
//            daoHelper.commit();
//        } catch (ProjectException e) {
//            LOG.error("DAO exception", e);
//            daoHelper.rollback();
//        } finally {
//            daoHelper.endTransaction();
//        }
//    }

    @Test
    public void testMultiCreate() {
        for (int i = 1; i < 11; i++) {
            final int number = i;
            new Thread() {
                public void run() {
                    DaoHelper daoHelper = new DaoHelper();
                    UserDao uDAO = new UserDao();
                    User user = new User();
                    user.setEmail("test"+ number +"@gmail.com");
                    user.setFirstName("test" + number);
                    user.setLastName("test" + number);
                    user.setRole(User.Role.CLIENT);
                    user.setLang(User.Lang.RU);
                    String password = "test"+ number +"@gmail.com";
                    user.setPassword(password);
                    try {
                        daoHelper.startTransaction(uDAO);
                        uDAO.create(user);
                        daoHelper.commit();
                        LOG.info("created user: " + user);
                    } catch (ProjectException e) {
                        LOG.error("DAO exception", e);
                        daoHelper.rollback();
                    } finally {
                        daoHelper.endTransaction();
                    }
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
        DaoHelper daoHelper = new DaoHelper();
        UserDao uDAO = new UserDao();
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setFirstName("test");
        user.setLastName("test");
        user.setRole(User.Role.CLIENT);
        user.setLang(User.Lang.RU);
        String password = "test@gmail.com";
        user.setPassword(password);
        try {
            daoHelper.startTransaction(uDAO);
            boolean flag = uDAO.create(user);
            daoHelper.commit();
            if (flag) {
                LOG.info("created user: " + user);
            }
        } catch (ProjectException e) {
            LOG.error("DAO exception", e);
            daoHelper.rollback();
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test
    public void testUpdateByEmailAndPassword() {
        DaoHelper daoHelper = new DaoHelper();
        UserDao uDAO = new UserDao();
        User user = new User();
        user.setEmail("example.48@gmail.com");
        user.setFirstName("xxx");
        user.setLastName("xxx");
        user.setLang(User.Lang.RU);
        user.setPassword("example.48@gmail.com");
        boolean result = false;
        try {
            daoHelper.startTransaction(uDAO);
            result = uDAO.update(user);
            daoHelper.commit();
        } catch (ProjectException e) {
            daoHelper.rollback();
        } finally {
            daoHelper.endTransaction();
        }
        LOG.debug(user);
        Assert.assertTrue(result);
    }

    @Test
    public void testDelete() {
        boolean result = false;
        DaoHelper daoHelper = new DaoHelper();
        UserDao uDAO = new UserDao();
        try {
            daoHelper.startTransaction(uDAO);
            result = uDAO.delete(50);
            daoHelper.commit();
        } catch (ProjectException e) {
            daoHelper.rollback();
        } finally {
            daoHelper.endTransaction();
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