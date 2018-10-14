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
import org.dbunit.IDatabaseTester;
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

    private static final ConnectionPoolDBUnit pool = ConnectionPoolDBUnit.POOL;

    @Test
    public void testFindAll() {
        DaoHelperDbUnit helperDbUnit = new DaoHelperDbUnit();
        EnrolleeDao enrolleeDAO = new EnrolleeDao();
        try {
            helperDbUnit.startTransaction(enrolleeDAO);
            for (Enrollee enrollee : enrolleeDAO.findAll()) {
                LOG.info(enrollee);
            }
        } catch (ProjectException e) {
            LOG.error("Test exception", e);
        } finally {
            helperDbUnit.endTransaction();
        }
    }

    @Test
    public void testRegisterToFacultyById() {
        DaoHelperDbUnit helperDbUnit = new DaoHelperDbUnit();
        EnrolleeDao enrolleeDAO = new EnrolleeDao();
        Enrollee enrollee = new Enrollee();
        enrollee.setId(2);
        for (int i = 201; i < 209; i++) {
            try {
                helperDbUnit.startTransaction(enrolleeDAO);
                enrolleeDAO.registerToFacultyById(enrollee.getId(), i);
                helperDbUnit.commit();
            } catch (ProjectException e) {
                helperDbUnit.rollback();
                LOG.error("DAO exception", e);
            } finally {
                helperDbUnit.endTransaction();
            }
        }
    }

    @Test
    public void testFindEntityById() {
        for (int i = 0; i < 10; i++) {
            new Thread() {
                public void run() {
                    DaoHelperDbUnit helperDbUnit = new DaoHelperDbUnit();
                    EnrolleeDao enrolleeDAO = new EnrolleeDao();
                    int id = (int)(Math.random() * 29 + 0.5);
                    try {
                        helperDbUnit.startTransaction(enrolleeDAO);
                        LOG.info("ID: " + id + " - " + enrolleeDAO.findEntityById(id));
                    } catch (ProjectException e) {
                        LOG.error("Test exception", e);
                    } finally {
                        helperDbUnit.endTransaction();
                    }
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
                    DaoHelperDbUnit helperDbUnit = new DaoHelperDbUnit();
                    EnrolleeDao enrolleeDAO = new EnrolleeDao();
                    Enrollee enrollee = new Enrollee();
                    enrollee.setCountry("Testerstan");
                    enrollee.setCity("Testerville" );
                    enrollee.setSchoolCertificate(100);
                    enrollee.setUserId(num);
                    try {
                        helperDbUnit.startTransaction(enrolleeDAO);
                        enrolleeDAO.create(enrollee);
                        helperDbUnit.commit();
                        LOG.info("created enrolle: " + enrollee);
                    } catch (ProjectException e) {
                        helperDbUnit.rollback();
                        LOG.error("DAO exception", e);
                    } finally {
                        helperDbUnit.endTransaction();
                    }
                }
            }.start();
        }
    }

    @BeforeClass
    public void setUpClass() throws Exception {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = builder.build(new File("resources/test-dataset_temp.xml"));
        IDatabaseTester tester = pool.getTester();
        tester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        tester.setTearDownOperation(DatabaseOperation.NONE);
        tester.setDataSet(dataSet);
        tester.onSetup();
    }

    @AfterClass
    public void tearDown() throws Exception {
        pool.getTester().onTearDown();
        pool.closePool();
        LOG.info("available connections: "
                + ConnectionPoolDBUnit.POOL.countAvailableConnections());
        LOG.info("used connections: "
                + ConnectionPoolDBUnit.POOL.countUsedConnections());
    }

    @Test
    public void testFindEnrolleeByUserId() {
    }

    @Test
    public void testFindEnrolleeStatus() {
        String failMessage = "Enrollee status finding failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        EnrolleeDao eDAO = new EnrolleeDao();
        try {
            daoHelper.startTransaction(eDAO);
            for (int i = 1; i < 5; i++) {
                for (int j = 201; j < 209; j++) {
                    LOG.info("Enrollee ID: " + i + " Faculty ID: " + j + " Status: " + eDAO.findEnrolleeStatus(i, j));
                }
            }
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test
    public void testFindBestEnrolleesIds() {
    }

    @Test
    public void testFindEnrolleeMarks() {
    }

    @Test
    public void testCheckFaculty() {
    }

    @Test
    public void testCheckAdmissionListEntry() {
    }

    @Test
    public void testAddSubject() {
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

    @Test
    public void testFindEnrolleesByFacultyId() {
        String failMessage = "Enrollees finding by faculty ID failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        EnrolleeDao eDAO = new EnrolleeDao();
        try {
            daoHelper.startTransaction(eDAO);
            for (int i = 201; i < 209; i++) {
                List<Enrollee> enrollees = eDAO.findEnrolleesByFacultyId(i);
                LOG.info(enrollees);
            }
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }
}