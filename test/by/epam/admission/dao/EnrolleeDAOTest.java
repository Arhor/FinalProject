package by.epam.admission.dao;

import by.epam.admission.exception.DAOException;
import by.epam.admission.model.Enrollee;
import by.epam.admission.model.User;
import by.epam.admission.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.*;

public class EnrolleeDAOTest {

    private static final Logger LOG = LogManager.getLogger(EnrolleeDAOTest.class);

    @Test
    public void testFindAll() {
//        TransactionHelper t = new TransactionHelper();
//        EnrolleeDAO enrolleeDAO = new EnrolleeDAO();
//        t.startTransaction(enrolleeDAO);
//        for (Enrollee enrollee : enrolleeDAO.findAll()) {
//            LOG.info(enrollee);
//        }
//        t.endTransaction();
    }

    @Test
    public void testFindEnrolleesByCountry() {
//        TransactionHelper t = new TransactionHelper();
//        EnrolleeDAO enrolleeDAO = new EnrolleeDAO();
//        t.startTransaction(enrolleeDAO);
//        for (Enrollee enrollee : enrolleeDAO.findEnrolleesByCountry("Беларусь")) {
//            LOG.info(enrollee);
//        }
//        t.endTransaction();
    }

    @Test
    public void testFindEnrolleesByCity() {
//        TransactionHelper t = new TransactionHelper();
//        EnrolleeDAO enrolleeDAO = new EnrolleeDAO();
//        t.startTransaction(enrolleeDAO);
//        for (Enrollee enrollee : enrolleeDAO.findEnrolleesByCity("Минск")) {
//            LOG.info(enrollee);
//        }
//        t.endTransaction();
    }

    @Test
    public void testFindEntityById() {
//        for (int i = 0; i < 100000; i++) {
//            new Thread() {
//                public void run() {
//                    TransactionHelper t = new TransactionHelper();
//                    EnrolleeDAO enrolleeDAO = new EnrolleeDAO();
//                    t.startTransaction(enrolleeDAO);
//                    int id = (int)(Math.random() * 55 + 0.5);
//                    LOG.info("ID: " + id + " - " + enrolleeDAO.findEntityById(id));
//                    t.endTransaction();
//                }
//            }.start();
//        }
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void testCreate() {
//        for (int i = 1; i < 50; i++) {
//            final int num = i;
//            new Thread() {
//                public void run() {
//                    TransactionHelper t = new TransactionHelper();
//                    EnrolleeDAO enrolleeDAO = new EnrolleeDAO();
//                    t.startTransaction(enrolleeDAO);
//                    Enrollee enrollee = new Enrollee();
//                    enrollee.setCountry("Testerstan");
//                    enrollee.setCity("Testerville" );
//                    enrollee.setSchoolCertificate(100);
//                    enrollee.setUserId(num);
//                    enrollee.setFacultyId((int)(Math.random() * 8 + 201));
//                    try {
//                        enrolleeDAO.create(enrollee);
//                        t.commit();
//                        LOG.info("created enrolle: " + enrollee);
//                    } catch (DAOException e) {
//                        LOG.error("DAO exception", e);
//                        t.rollback();
//                    }
//                    t.endTransaction();
//                }
//            }.start();
//        }
    }

    @BeforeClass
    public void setUp() {
    }

    @AfterClass
    public void tearDown() {
//        ConnectionPool.POOL.closeConnections();
//        LOG.info("available connections: "
//                + ConnectionPool.POOL.countAvailableConnections());
//        LOG.info("used connections: "
//                + ConnectionPool.POOL.countUsedConnections());
    }
}