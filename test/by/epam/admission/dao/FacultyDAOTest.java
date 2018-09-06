/*
 * class: FacultyDAOTest
 */

package by.epam.admission.dao;

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
public class FacultyDAOTest {

    private static final Logger LOG = LogManager.getLogger(FacultyDAOTest.class);

    private FacultyDAO facultyDao;
    private TransactionHelper transactionHelper;

    @Test
    public void findAllTest() {
        transactionHelper.startTransaction(facultyDao);
        for (Faculty faculty : facultyDao.findAll()) {
            LOG.info(faculty);
        }
        transactionHelper.endTransaction();
    }

    @Test
    public void findEntityByIdTest() {
        transactionHelper.startTransaction(facultyDao);
        LOG.info(facultyDao.findEntityById(201));
        transactionHelper.endTransaction();
    }

    @Test
    public void findFacultyBySubjectIdTest() {
        transactionHelper.startTransaction(facultyDao);
        for (Faculty faculty : facultyDao.findFacultiesBySubjectId(101)) {
            LOG.info(faculty);
        }
        transactionHelper.endTransaction();
    }

    @Test
    public void createTest() {
        Faculty faculty = new Faculty();
        faculty.setId(201);
        faculty.setNameRu("Тестовый факультет");
        faculty.setNameEn("Test faculty");
        faculty.setSeatsTotal(200);
        faculty.setSeatsBudget(50);
        transactionHelper.startTransaction(facultyDao);
        boolean result = facultyDao.create(faculty);
        transactionHelper.endTransaction();
        Assert.assertFalse(result);
    }

    @BeforeClass
    public void setUp() {
        facultyDao = new FacultyDAO();
        transactionHelper = new TransactionHelper();
    }

    @AfterClass
    public void tearDown() {
        facultyDao = null;
        transactionHelper = null;
        ConnectionPool.POOL.closeConnections();
    }
}
