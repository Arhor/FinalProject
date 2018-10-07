/*
 * class: FacultyDaoTest
 */

package by.epam.admission.dao.impl;

import by.epam.admission.dao.DaoHelper;
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
    private DaoHelper daoHelper;

    @Test
    public void findAllTest() {
        try {
            daoHelper.startTransaction(facultyDao);
            for (Faculty faculty : facultyDao.findAll()) {
                LOG.info(faculty);
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
            daoHelper.startTransaction(facultyDao);
            LOG.info(facultyDao.findEntityById(201));
        } catch (ProjectException e) {
            LOG.error("Test exception", e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test
    public void createTest() {
        Faculty faculty = new Faculty();
        faculty.setId(201);
        faculty.setNameRu("Тестовый факультет");
        faculty.setNameEn("Test faculty");
        faculty.setSeatsPaid(200);
        faculty.setSeatsBudget(50);
        boolean result = false;
        try {
            daoHelper.startTransaction(facultyDao);
            result = facultyDao.create(faculty);
            daoHelper.commit();
        } catch (ProjectException e) {
            LOG.error("DAO exception", e);
            daoHelper.rollback();
        } finally {
            daoHelper.endTransaction();
        }
        Assert.assertFalse(result);
    }

    @BeforeClass
    public void setUp() {
        facultyDao = new FacultyDao();
        daoHelper = new DaoHelper();
    }

    @AfterClass
    public void tearDown() {
        facultyDao = null;
        daoHelper = null;
        ConnectionPool.POOL.closePool();
    }
}
