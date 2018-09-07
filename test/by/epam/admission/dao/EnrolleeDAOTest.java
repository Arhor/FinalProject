package by.epam.admission.dao;

import by.epam.admission.dao.impl.EnrolleeDAO;
import by.epam.admission.exception.DAOException;
import by.epam.admission.model.Enrollee;
import by.epam.admission.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.*;

public class EnrolleeDAOTest {

    private static final Logger LOG = LogManager.getLogger(EnrolleeDAOTest.class);

    @Test
    public void testFindAll() {
        TransactionHelper t = new TransactionHelper();
        EnrolleeDAO enrolleeDAO = new EnrolleeDAO();
        t.startTransaction(enrolleeDAO);
        for (Enrollee enrollee : enrolleeDAO.findAll()) {
            LOG.info(enrollee);
        }
        t.endTransaction();
    }

    @Test
    public void testFindEnrolleesByCountry() {
        TransactionHelper t = new TransactionHelper();
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
        TransactionHelper t = new TransactionHelper();
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
        TransactionHelper t = new TransactionHelper();
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
                    TransactionHelper t = new TransactionHelper();
                    EnrolleeDAO enrolleeDAO = new EnrolleeDAO();
                    t.startTransaction(enrolleeDAO);
                    int id = (int)(Math.random() * 55 + 0.5);
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
                    TransactionHelper t = new TransactionHelper();
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
        TransactionHelper t = new TransactionHelper();
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