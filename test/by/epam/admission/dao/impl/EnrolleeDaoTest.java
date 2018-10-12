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
import org.testng.annotations.*;

import java.io.File;

public class EnrolleeDaoTest {

    private static final Logger LOG = LogManager.getLogger(
            EnrolleeDaoTest.class);

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
    public void testFindEnrolleesByCountry() {
        DaoHelperDbUnit helperDbUnit = new DaoHelperDbUnit();
        EnrolleeDao enrolleeDAO = new EnrolleeDao();
        try {
            helperDbUnit.startTransaction(enrolleeDAO);
            for (Enrollee enrollee : enrolleeDAO.findEnrolleesByCountry("Беларусь")) {
                LOG.info(enrollee);
            }
        } catch (ProjectException e) {
            LOG.error("DAO exception", e);
        } finally {
            helperDbUnit.endTransaction();
        }
    }

    @Test
    public void testFindEnrolleesByCity() {
        DaoHelperDbUnit helperDbUnit = new DaoHelperDbUnit();
        EnrolleeDao enrolleeDAO = new EnrolleeDao();
        try {
            helperDbUnit.startTransaction(enrolleeDAO);
            for (Enrollee enrollee : enrolleeDAO.findEnrolleesByCity("Минск")) {
                LOG.info(enrollee);
            }
        } catch (ProjectException e) {
            LOG.error("DAO exception", e);
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

    @Test
    public void testDelete() {
        DaoHelperDbUnit helperDbUnit = new DaoHelperDbUnit();
        EnrolleeDao enrolleeDAO = new EnrolleeDao();
        Enrollee enrollee = new Enrollee();
        enrollee.setId(2);
        try {
            helperDbUnit.startTransaction(enrolleeDAO);
            LOG.info(enrolleeDAO.delete(enrollee));
            helperDbUnit.commit();
        } catch (ProjectException e) {
            helperDbUnit.rollback();
            LOG.error("DAO exception", e);
        } finally {
            helperDbUnit.endTransaction();
        }
    }


    @BeforeClass
    public void setUpClass() {
        IDatabaseTester tester = ConnectionPoolDBUnit.POOL.getTester();
        tester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        tester.setTearDownOperation(DatabaseOperation.NONE);
    }

    @AfterClass
    public void tearDown() {
        ConnectionPoolDBUnit.POOL.closePool();
        LOG.info("available connections: "
                + ConnectionPoolDBUnit.POOL.countAvailableConnections());
        LOG.info("used connections: "
                + ConnectionPoolDBUnit.POOL.countUsedConnections());
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = builder.build(new File("resources/test-dataset_temp.xml"));
        ConnectionPoolDBUnit.POOL.getTester().setDataSet(dataSet);
        ConnectionPoolDBUnit.POOL.getTester().onSetup();
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
        ConnectionPoolDBUnit.POOL.getTester().onTearDown();
    }


}