/*
 * class: SubjectDAOTest
 */

package by.epam.admission.dao;

import by.epam.admission.model.Subject;
import by.epam.admission.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.*;

/**
 * @author Maxim Burishinets
 * @version 1.0 30 Aug 2018
 */
public class SubjectDAOTest {

    private static final Logger LOG = LogManager.getLogger(SubjectDAOTest.class);

    private SubjectDAO subjectDao;
    private TransactionHelper transactionHelper;

    @BeforeClass
    public void setUp() {
//        subjectDao = new SubjectDAO();
//        transactionHelper = new TransactionHelper();
    }

    @AfterClass
    public void tearDown() {
//        subjectDao = null;
//        transactionHelper = null;
//        ConnectionPool.POOL.closeConnections();
    }

    @Test
    public void testFindAll() {
//        transactionHelper.startTransaction(subjectDao);
//        for (Subject subject : subjectDao.findAll()) {
//            LOG.info(subject);
//        }
//        transactionHelper.endTransaction();
    }

    @Test
    public void findEntityByIdTest() {
//        transactionHelper.startTransaction(subjectDao);
//        LOG.info(subjectDao.findEntityById(101));
//        transactionHelper.endTransaction();
    }

    @Test
    public void findFacultyBySubjectIdTest() {
//        transactionHelper.startTransaction(subjectDao);
//        for (Subject subject : subjectDao.findSubjectsByFacultyId(205)) {
//            LOG.info(subject);
//        }
//        transactionHelper.endTransaction();
    }

    @Test
    public void testFindEntityById() {
    }

    @Test
    public void testDelete() {
    }

    @Test
    public void testDelete1() {
    }

    @Test
    public void testCreate() {
    }

    @Test
    public void testUpdate() {
    }
}