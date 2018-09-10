/*
 * class: SubjectDaoTest
 */

package by.epam.admission.dao.impl;

import by.epam.admission.dao.TransactionHelper;
import by.epam.admission.model.Subject;
import by.epam.admission.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.*;

/**
 * @author Maxim Burishinets
 * @version 1.0 30 Aug 2018
 */
public class SubjectDaoTest {

    private static final Logger LOG = LogManager.getLogger(SubjectDaoTest.class);

    private SubjectDao subjectDao;
    private TransactionHelper transactionHelper;

    @BeforeClass
    public void setUp() {
        subjectDao = new SubjectDao();
        transactionHelper = new TransactionHelper();
    }

    @AfterClass
    public void tearDown() {
        subjectDao = null;
        transactionHelper = null;
        ConnectionPool.POOL.closePool();
    }

    @Test
    public void testFindAll() {
        transactionHelper.startTransaction(subjectDao);
        for (Subject subject : subjectDao.findAll()) {
            LOG.info(subject);
        }
        transactionHelper.endTransaction();
    }

    @Test
    public void findEntityByIdTest() {
        transactionHelper.startTransaction(subjectDao);
        LOG.info(subjectDao.findEntityById(101));
        transactionHelper.endTransaction();
    }

    @Test
    public void findFacultyBySubjectIdTest() {
        transactionHelper.startTransaction(subjectDao);
        for (Subject subject : subjectDao.findSubjectsByFacultyId(205)) {
            LOG.info(subject);
        }
        transactionHelper.endTransaction();
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