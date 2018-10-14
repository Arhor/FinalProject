/*
 * class: EnrolleeDaoTest
 */

package by.epam.admission.dao.impl;

import by.epam.admission.dao.DaoHelperDbUnit;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Enrollee;
import by.epam.admission.pool.ConnectionPoolDBUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.util.List;

/**
 * @author Burishinets Maxim
 * @version 1.0 29 Aug 2018
 */
public class EnrolleeDaoTest {

    private static final Logger LOG = LogManager.getLogger(
            EnrolleeDaoTest.class);

    private static ConnectionPoolDBUnit pool = ConnectionPoolDBUnit.POOL;

    @Test
    public void testFindAll() {
        String failMessage = "";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        EnrolleeDao eDAO = new EnrolleeDao();
        try {
            daoHelper.startTransaction(eDAO);
            int pageNumber = 0;
            int rowsPerPage = 10;
            List<Enrollee> actual = eDAO.findAll(pageNumber, rowsPerPage);
            for (Enrollee enrollee : actual) {
                LOG.info(enrollee);
            }
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test
    public void testFindEntityById() {
    }

    @Test
    public void testFindEnrolleeByUserId() {
    }

    @Test
    public void testFindEnrolleesByFacultyId() {
    }

    @Test
    public void testFindEnrolleeStatus() {
    }

    @Test
    public void testFindBestEnrolleesIds() {
    }

    @Test
    public void testFindEnrolleeMarks() {
    }

    @Test
    public void testCheckStatus() {
    }

    @Test
    public void testCheckFaculty() {
    }

    @Test
    public void testCheckAdmissionListStatus() {
    }

    @Test
    public void testDelete() {
    }

    @Test
    public void testCreate() {
    }

    @Test
    public void testAddSubject() {
    }

    @Test
    public void testRegisterToFacultyById() {
    }

    @Test
    public void testDeregisterFromFacultyById() {
    }

    @Test
    public void testRestoreFacultyRegistration() {
    }

    @Test
    public void testUpdateAdmissionList() {
    }

    @Test
    public void testUpdate() {
    }

    @BeforeClass
    public void setUpClass() throws Exception {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = builder.build(
                new File("resources/test-dataset_temp.xml"));
        pool.getTester().setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        pool.getTester().setTearDownOperation(DatabaseOperation.NONE);
        pool.getTester().setDataSet(dataSet);
        pool.getTester().onSetup();
    }

    @AfterClass
    public void tearDownClass() throws Exception {
        pool.getTester().onTearDown();
        pool.closePool();
    }

    @DataProvider(name = "enrolleesDataSet")
    public Object[][] createSelectionData() {
        return new Object[][] {
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {}
        };
    }

}