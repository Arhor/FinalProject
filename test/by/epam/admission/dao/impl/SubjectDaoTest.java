/*
 * class: SubjectDaoTest
 */

package by.epam.admission.dao.impl;

import by.epam.admission.dao.DaoHelper;
import by.epam.admission.exception.ProjectException;
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
    private DaoHelper daoHelper;

    @BeforeClass
    public void setUp() {
        subjectDao = new SubjectDao();
        daoHelper = new DaoHelper();
    }

    @AfterClass
    public void tearDown() {
        subjectDao = null;
        daoHelper = null;
        ConnectionPool.POOL.closePool();
    }

    @Test
    public void testFindAll() {
        try {
            daoHelper.startTransaction(subjectDao);
            for (Subject subject : subjectDao.findAll()) {
                LOG.info(subject);
            }
        } catch (ProjectException e) {
            LOG.error("Test exception", e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test
    public void findEntityByIdTest() {
        try {
            daoHelper.startTransaction(subjectDao);
            LOG.info(subjectDao.findEntityById(101));
        } catch (ProjectException e) {
            LOG.error("Test exception", e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test
    public void findFacultyBySubjectIdTest() {
        try {
            daoHelper.startTransaction(subjectDao);
            for (Subject subject : subjectDao.findSubjectsByFacultyId(205)) {
                LOG.info(subject);
            }
        } catch (ProjectException e) {
            LOG.error("Test exception", e);
        } finally {
            daoHelper.endTransaction();
        }
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