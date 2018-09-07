package by.epam.admission.dao;

import by.epam.admission.dao.impl.EnrolleeDAO;
import by.epam.admission.exception.DAOException;
import by.epam.admission.model.Enrollee;
import by.epam.admission.pool.ConnectionPool;
import by.epam.admission.pool.ConnectionPoolDBUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.testng.annotations.*;

import java.io.File;

public class EnrolleeDAOTest {

    private static final Logger LOG = LogManager.getLogger(EnrolleeDAOTest.class);

    @Test
    public void testFindAll() {
        TransactionHelperDBUnit t = new TransactionHelperDBUnit();
        EnrolleeDAO enrolleeDAO = new EnrolleeDAO();
        t.startTransaction(enrolleeDAO);
        for (Enrollee enrollee : enrolleeDAO.findAll()) {
            LOG.info(enrollee);
        }
        t.endTransaction();
    }

    @Test
    public void testFindEnrolleesByCountry() {
        TransactionHelperDBUnit t = new TransactionHelperDBUnit();
        EnrolleeDAO enrolleeDAO = new EnrolleeDAO();
        t.startTransaction(enrolleeDAO);
        try {
            for (Enrollee enrollee : enrolleeDAO.findEnrolleesByCountry("Беларусь")) {
                LOG.info(enrollee);
            }
        } catch (DAOException e) {
            LOG.error("DAO exception", e);
        }
        t.endTransaction();
    }

    @Test
    public void testFindEnrolleesByCity() {
        TransactionHelperDBUnit t = new TransactionHelperDBUnit();
        EnrolleeDAO enrolleeDAO = new EnrolleeDAO();
        t.startTransaction(enrolleeDAO);
        try {
            for (Enrollee enrollee : enrolleeDAO.findEnrolleesByCity("Минск")) {
                LOG.info(enrollee);
            }
        } catch (DAOException e) {
            LOG.error("DAO exception", e);
        }
        t.endTransaction();
    }

    @Test
    public void testRegisterToFacultyById() {
        TransactionHelperDBUnit t = new TransactionHelperDBUnit();
        EnrolleeDAO enrolleeDAO = new EnrolleeDAO();
        Enrollee enrollee = new Enrollee();
        enrollee.setId(2);
        for (int i = 201; i < 209; i++) {
            t.startTransaction(enrolleeDAO);
            try {
                enrolleeDAO.registerToFacultyById(enrollee, i);
                t.commit();
            } catch (DAOException e) {
                t.rollback();
                LOG.error("DAO exception", e);
            }
            t.endTransaction();
        }
    }

    @Test
    public void testFindEntityById() {
        for (int i = 0; i < 10; i++) {
            new Thread() {
                public void run() {
                    TransactionHelperDBUnit t = new TransactionHelperDBUnit();
                    EnrolleeDAO enrolleeDAO = new EnrolleeDAO();
                    t.startTransaction(enrolleeDAO);
                    int id = (int)(Math.random() * 29 + 0.5);
                    LOG.info("ID: " + id + " - " + enrolleeDAO.findEntityById(id));
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
        for (int i = 1; i < 50; i++) {
            final int num = i;
            new Thread() {
                public void run() {
                    TransactionHelperDBUnit t = new TransactionHelperDBUnit();
                    EnrolleeDAO enrolleeDAO = new EnrolleeDAO();
                    t.startTransaction(enrolleeDAO);
                    Enrollee enrollee = new Enrollee();
                    enrollee.setCountry("Testerstan");
                    enrollee.setCity("Testerville" );
                    enrollee.setSchoolCertificate(100);
                    enrollee.setUserId(num);
                    try {
                        enrolleeDAO.create(enrollee);
                        t.commit();
                        LOG.info("created enrolle: " + enrollee);
                    } catch (DAOException e) {
                        t.rollback();
                        LOG.error("DAO exception", e);
                    }
                    t.endTransaction();
                }
            }.start();
        }
    }

    @Test
    public void testDelete() {
        TransactionHelperDBUnit t = new TransactionHelperDBUnit();
        EnrolleeDAO enrolleeDAO = new EnrolleeDAO();
        Enrollee enrollee = new Enrollee();
        enrollee.setId(2);
        t.startTransaction(enrolleeDAO);
        try {
            LOG.info(enrolleeDAO.delete(enrollee));
            t.commit();
        } catch (DAOException e) {
            t.rollback();
            LOG.error("DAO exception", e);
        }
        t.endTransaction();
    }


    @BeforeClass
    public void setUpClass() {
        ConnectionPoolDBUnit.POOL.tester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        ConnectionPoolDBUnit.POOL.tester.setTearDownOperation(DatabaseOperation.NONE);
    }

    @AfterClass
    public void tearDown() {
        LOG.info("available connections: "
                + ConnectionPool.POOL.countAvailableConnections());
        LOG.info("used connections: "
                + ConnectionPool.POOL.countUsedConnections());
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = builder.build(new File("resources/all-tables-dataset.xml"));
        ConnectionPoolDBUnit.POOL.tester.setDataSet(dataSet);
        ConnectionPoolDBUnit.POOL.tester.onSetup();
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
        ConnectionPoolDBUnit.POOL.tester.onTearDown();
    }


}