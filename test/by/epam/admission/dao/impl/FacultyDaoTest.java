/*
 * class: FacultyDaoTest
 */

package by.epam.admission.dao.impl;

import by.epam.admission.dao.TransactionHelper;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Faculty;
import by.epam.admission.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Maxim Burishinets
 * @version 1.0 29 Aug 2018
 */
public class FacultyDaoTest {

    private static final Logger LOG = LogManager.getLogger(FacultyDaoTest.class);

    private FacultyDao facultyDao;
    private TransactionHelper transactionHelper;

    @Test
    public void findAllTest() {
        transactionHelper.startTransaction(facultyDao);
        try {
            for (Faculty faculty : facultyDao.findAll()) {
                LOG.info(faculty);
            }
        } catch (ProjectException e) {
            LOG.error("Test exception", e);
        }
        transactionHelper.endTransaction();
    }

    @Test
    public void findEntityByIdTest() {
        transactionHelper.startTransaction(facultyDao);
        try {
            LOG.info(facultyDao.findEntityById(201));
        } catch (ProjectException e) {
            LOG.error("Test exception", e);
        }
        transactionHelper.endTransaction();
    }

    @Test
    public void findFacultyBySubjectIdTest() {
        transactionHelper.startTransaction(facultyDao);
        try {
            for (Faculty faculty : facultyDao.findFacultiesBySubjectId(101)) {
                LOG.info(faculty);
            }
        } catch (ProjectException e) {
            LOG.error("Test exception", e);
        }
        transactionHelper.endTransaction();
    }

    @Test
    public void createTest() {
        Faculty faculty = new Faculty();
        faculty.setId(201);
        faculty.setNameRu("Тестовый факультет");
        faculty.setNameEn("Test faculty");
        faculty.setSeatsPaid(200);
        faculty.setSeatsBudget(50);
        transactionHelper.startTransaction(facultyDao);
        boolean result = false;
        try {
            result = facultyDao.create(faculty);
            transactionHelper.commit();
        } catch (ProjectException e) {
            LOG.error("DAO exception", e);
            transactionHelper.rollback();
        }
        transactionHelper.endTransaction();
        Assert.assertFalse(result);
    }

    @BeforeClass
    public void setUp() {
        facultyDao = new FacultyDao();
        transactionHelper = new TransactionHelper();
    }

    @AfterClass
    public void tearDown() {
        facultyDao = null;
        transactionHelper = null;
        ConnectionPool.POOL.closePool();
    }
}
