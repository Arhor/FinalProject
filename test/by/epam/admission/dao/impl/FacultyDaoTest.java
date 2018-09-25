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
        daoHelper.startTransaction(facultyDao);
        try {
            for (Faculty faculty : facultyDao.findAll()) {
                LOG.info(faculty);
            }
        } catch (ProjectException e) {
            LOG.error("Test exception", e);
        }
        daoHelper.endTransaction();
    }

    @Test
    public void findEntityByIdTest() {
        daoHelper.startTransaction(facultyDao);
        try {
            LOG.info(facultyDao.findEntityById(201));
        } catch (ProjectException e) {
            LOG.error("Test exception", e);
        }
        daoHelper.endTransaction();
    }

    @Test
    public void findFacultyBySubjectIdTest() {
        daoHelper.startTransaction(facultyDao);
        try {
            for (Faculty faculty : facultyDao.findFacultiesBySubjectId(101)) {
                LOG.info(faculty);
            }
        } catch (ProjectException e) {
            LOG.error("Test exception", e);
        }
        daoHelper.endTransaction();
    }

    @Test
    public void createTest() {
        Faculty faculty = new Faculty();
        faculty.setId(201);
        faculty.setNameRu("Тестовый факультет");
        faculty.setNameEn("Test faculty");
        faculty.setSeatsPaid(200);
        faculty.setSeatsBudget(50);
        daoHelper.startTransaction(facultyDao);
        boolean result = false;
        try {
            result = facultyDao.create(faculty);
            daoHelper.commit();
        } catch (ProjectException e) {
            LOG.error("DAO exception", e);
            daoHelper.rollback();
        }
        daoHelper.endTransaction();
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
