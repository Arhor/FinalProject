/*
 * class: UserDaoTest
 */

package by.epam.admission.dao.impl;

import by.epam.admission.dao.DaoHelper;
import by.epam.admission.dao.DaoHelperDbUnit;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.User;
import by.epam.admission.pool.ConnectionPool;
import by.epam.admission.pool.ConnectionPoolDBUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;

/**
 * @author Maxim Burishinets
 * @version 1.0 3 Sep 2018
 */
public class UserDaoTest {

    private static final Logger LOG = LogManager.getLogger(UserDaoTest.class);

    private static ConnectionPoolDBUnit pool = ConnectionPoolDBUnit.POOL;

    @Test(dataProvider = "usersDataSet", description = "positive test")
    public void testFindEntityById(User currUser) {
        String failMessage = "User finding test failed";
        DaoHelper daoHelper = new DaoHelper();
        UserDao uDAO = new UserDao();
        try {
            daoHelper.startTransaction(uDAO);
            User user = uDAO.findEntityById(currUser.getId());
            LOG.info("expected: " + currUser);
            LOG.info("  actual: " + user);
            Assert.assertEquals(user, currUser, failMessage);
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "usersToInsert", description = "positive test")
    public void testCreatePositive(User currUser) {
        String failMessage = "User creation positive test failed";
        DaoHelper daoHelper = new DaoHelper();
        UserDao uDAO = new UserDao();
        String password = currUser.getEmail();
        currUser.setPassword(password);
        try {
            daoHelper.startTransaction(uDAO);
            boolean result = uDAO.create(currUser);
            if (result) {
                User user = uDAO.findEntityById(currUser.getId());
                currUser.setId(user.getId());
                LOG.info("expected: " + currUser);
                LOG.info("  actual: " + user);
                Assert.assertEquals(user, currUser, failMessage);
            } else {
                Assert.fail(failMessage);
            }
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.rollback();
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "usersDataSet", description = "negative test")
    public void testCreateNegative(User currUser) {
        String failMessage = "User creation negative test failed";
        DaoHelper daoHelper = new DaoHelper();
        UserDao uDAO = new UserDao();
        String password = currUser.getEmail();
        currUser.setPassword(password);
        try {
            daoHelper.startTransaction(uDAO);
            boolean result = uDAO.create(currUser);
            Assert.assertFalse(result, failMessage);
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.rollback();
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "usersDataSet", description = "positive test")
    public void testDeletePositive(User currUser) {
        String failMessage = "User deletion positive test failed";
        DaoHelper daoHelper = new DaoHelper();
        UserDao uDAO = new UserDao();
        try {
            daoHelper.startTransaction(uDAO);
            boolean result = uDAO.delete(currUser.getId());
            if (result) {
                boolean status = uDAO.checkUser(currUser.getId());
                Assert.assertFalse(status, failMessage);
            } else {
                Assert.fail(failMessage);
            }
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.rollback();
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "usersDataSet", description = "positive test")
    public void testUpdatePositive(User currUser) {
        String failMessage = "User update positive test failed";
        DaoHelper daoHelper = new DaoHelper();
        UserDao uDAO = new UserDao();
        try {
            daoHelper.startTransaction(uDAO);
            User user = currUser.clone();
            user.setFirstName("TEST_FIRST_NAME");
            user.setLastName("LAST_LAST_NAME");
            user.setPassword(user.getEmail());
            boolean result = uDAO.update(user);
            if (result) {
                User updatedUser = uDAO.findEntityById(user.getId());
                Assert.assertEquals(updatedUser, user, failMessage);
            } else {
                Assert.fail(failMessage);
            }
        } catch (ProjectException | CloneNotSupportedException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.rollback();
            daoHelper.endTransaction();
        }
    }


    @BeforeClass
    public void setUpClass() throws Exception {
        IDatabaseTester tester = pool.getTester();
        tester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        tester.setTearDownOperation(DatabaseOperation.NONE);
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = builder.build(
                new File("resources/test-dataset_temp.xml"));
        pool.getTester().setDataSet(dataSet);
        pool.getTester().onSetup();
    }

    @AfterClass
    public void tearDownClass() throws Exception {
        pool.getTester().onTearDown();
        pool.closePool();
    }

    @DataProvider(name = "usersDataSet")
    public static Object[][] createSelectionData() {
        return new Object[][] {
                {new User(1, "MaxFactor", "Brown", "example.1@gmail.com", User.Lang.EN, User.Role.ADMIN)},
                {new User(2, "Тимофей", "Королёв", "example.2@gmail.com", User.Lang.RU, User.Role.CLIENT)},
                {new User(3, "Archie", "Edwards", "example.3@gmail.com", User.Lang.EN, User.Role.CLIENT)},
                {new User(4, "Антон", "Устинов", "example.4@gmail.com", User.Lang.RU, User.Role.CLIENT)},
                {new User(5, "Георгий", "Королёв", "example.5@gmail.com", User.Lang.RU, User.Role.CLIENT)},
                {new User(6, "Samuel", "Wright", "example.6@gmail.com", User.Lang.EN, User.Role.CLIENT)},
                {new User(7, "Arthur", "White", "example.7@gmail.com", User.Lang.EN, User.Role.CLIENT)},
                {new User(8, "Max", "Smith", "example.8@gmail.com", User.Lang.EN, User.Role.CLIENT)},
                {new User(9, "Борис", "Абрамов", "example.9@gmail.com", User.Lang.RU, User.Role.CLIENT)},
                {new User(10, "Max", "Davies", "example.10@gmail.com", User.Lang.EN, User.Role.CLIENT)},
        };
    }

    @DataProvider(name = "usersToInsert")
    public static Object[][] createInsertionData() {
        return new Object[][] {
                {new User("MaxFactor", "Brown", "example.101@gmail.com", User.Lang.EN, User.Role.CLIENT)},
                {new User("Тимофей", "Королёв", "example.102@gmail.com", User.Lang.RU, User.Role.CLIENT)},
                {new User("Archie", "Edwards", "example.103@gmail.com", User.Lang.EN, User.Role.CLIENT)},
                {new User("Антон", "Устинов", "example.104@gmail.com", User.Lang.RU, User.Role.CLIENT)},
                {new User("Георгий", "Королёв", "example.105@gmail.com", User.Lang.RU, User.Role.CLIENT)},
                {new User("Samuel", "Wright", "example.106@gmail.com", User.Lang.EN, User.Role.CLIENT)},
                {new User("Arthur", "White", "example.107@gmail.com", User.Lang.EN, User.Role.CLIENT)},
                {new User("Max", "Smith", "example.108@gmail.com", User.Lang.EN, User.Role.CLIENT)},
                {new User("Борис", "Абрамов", "example.109@gmail.com", User.Lang.RU, User.Role.CLIENT)},
                {new User("Max", "Davies", "example.110@gmail.com", User.Lang.EN, User.Role.CLIENT)},
        };
    }

}