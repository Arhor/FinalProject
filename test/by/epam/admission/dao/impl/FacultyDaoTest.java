/*
 * class: FacultyDaoTest
 */

package by.epam.admission.dao.impl;

import by.epam.admission.dao.DaoHelperDbUnit;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Faculty;
import by.epam.admission.pool.ConnectionPoolDBUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Maxim Burishinets
 * @version 1.0 29 Aug 2018
 */
public class FacultyDaoTest {

    private static final Logger LOG = LogManager.getLogger(FacultyDaoTest.class);

    private static ConnectionPoolDBUnit pool = ConnectionPoolDBUnit.POOL;

    @Test(dataProvider = "facultiesList", description = "positive test")
    public void testFindAll(List<Faculty> expected) {
        String failMessage = "Faculty finding all test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        FacultyDao fDAO = new FacultyDao();
        try {
            daoHelper.startTransaction(fDAO);
            List<Faculty> actual = fDAO.findAll();
            Assert.assertEquals(actual, expected, failMessage);
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "facultiesDataSet", description = "positive test")
    public void testFindEntityById(Faculty expected) {
        String failMessage = "Faculty finding test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        FacultyDao fDAO = new FacultyDao();
        try {
            daoHelper.startTransaction(fDAO);
            Faculty actual = fDAO.findEntityById(expected.getId());
            Assert.assertEquals(actual, expected, failMessage);
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "facultiesDataSet", description = "positive test")
    public void testCheckStatus(Faculty faculty) {
        String failMessage = "Faculty status checking test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        FacultyDao fDAO = new FacultyDao();
        try {
            daoHelper.startTransaction(fDAO);
            boolean result = fDAO.checkStatus(faculty.getId());
            LOG.info("Faculty ID: " + faculty.getId() + " Status: " + result);
            Assert.assertTrue(result, failMessage);
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "facultiesDataSet", description = "positive test")
    public void testDelete(Faculty faculty) {
        String failMessage = "Faculty status checking test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        FacultyDao fDAO = new FacultyDao();
        try {
            daoHelper.startTransaction(fDAO);
            boolean result = fDAO.delete(faculty.getId());
            boolean status = fDAO.checkStatus(faculty.getId());
            LOG.info("Deletion result: " + result);
            LOG.info("Faculty status: " + status);
            if (!result || status) {
                Assert.fail(failMessage);
            }
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.rollback();
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "facultiesToInsert", description = "positive test")
    public void testCreate(Faculty faculty) {
        String failMessage = "Faculty create test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        FacultyDao fDAO = new FacultyDao();
        try {
            daoHelper.startTransaction(fDAO);
            boolean result = fDAO.create(faculty);
            Faculty inserted = fDAO.findEntityById(faculty.getId());
            boolean equals =  faculty.equals(inserted);
            LOG.info("Insertion result: " + result);
            LOG.info("Equivalency: " + equals);
            if (!result || !equals) {
                LOG.info(faculty);
                LOG.info(inserted);
                Assert.fail(failMessage);
            }
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.rollback();
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "facultiesDataSet", description = "positive test")
    public void testUpdate(Faculty faculty) {
        String failMessage = "Faculty update test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        FacultyDao fDAO = new FacultyDao();
        try {
            Faculty expected = faculty.clone();
            daoHelper.startTransaction(fDAO);
            expected.setNameRu("Test_RU");
            expected.setNameEn("Test_EN");
            expected.setChecked(true);
            expected.setSeatsBudget(99);
            expected.setSeatsBudget(99);
            boolean result = fDAO.update(expected);
            Faculty updated = fDAO.findEntityById(expected.getId());
            boolean equals =  expected.equals(updated);
            LOG.info("Update result: " + result);
            LOG.info("Equivalency: " + equals);
            if (!result || !equals) {
                Assert.fail(failMessage);
            }
        } catch (ProjectException | CloneNotSupportedException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.rollback();
            daoHelper.endTransaction();
        }
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

    @DataProvider(name = "facultiesDataSet", parallel = true)
    public Object[][] createSelectionData() {
        return new Object[][] {
                {new Faculty(201, "Факультет компьютерного проектирования", "Faculty of computer-aided design", 5, 3)},
                {new Faculty(202, "Факультет информационных технологий и управления", "Faculty of information technologies and control", 4, 2)},
                {new Faculty(203, "Факультет радиотехники и электроники", "Faculty of radioengineering and electronics", 2, 4)},
                {new Faculty(204, "Факультет компьютерных систем и сетей", "Faculty of computer systems and networks", 3, 3)},
                {new Faculty(205, "Факультет инфокоммуникаций", "Faculty of infocommunications", 4, 2)},
                {new Faculty(206, "Инженерно-экономический факультет", "Faculty of engineering and economics", 3, 4)},
                {new Faculty(207, "Факультет инновационного непрерывного образования", "Faculty of innovative lifelong learning", 5, 2)},
                {new Faculty(208, "Военный факультет", "Military faculty", 5, 4)}
        };
    }

    @DataProvider(name = "facultiesToInsert", parallel = true)
    public Object[][] createInsertionData() {
        return new Object[][] {
                {new Faculty(211, "Тест 1", "Test 1", 1, 2)},
                {new Faculty(212, "Тест 2", "Test 2", 3, 4)},
                {new Faculty(213, "Тест 3", "Test 3", 5, 6)},
                {new Faculty(214, "Тест 4", "Test 4", 7, 8)},
                {new Faculty(215, "Тест 5", "Test 5", 9, 10)},
                {new Faculty(216, "Тест 6", "Test 6", 11, 12)},
                {new Faculty(217, "Тест 7", "Test 7", 13, 14)},
                {new Faculty(218, "Тест 8", "Test 8", 15, 16)}
        };
    }

    @DataProvider(name = "facultiesList")
    public Object[][] createData() {
        return new Object[][] {
                {
                    new ArrayList<Faculty>() {
                        {
                            add(new Faculty(201, "Факультет компьютерного проектирования", "Faculty of computer-aided design", 5, 3));
                            add(new Faculty(202, "Факультет информационных технологий и управления", "Faculty of information technologies and control", 4, 2));
                            add(new Faculty(203, "Факультет радиотехники и электроники", "Faculty of radioengineering and electronics", 2, 4));
                            add(new Faculty(204, "Факультет компьютерных систем и сетей", "Faculty of computer systems and networks", 3, 3));
                            add(new Faculty(205, "Факультет инфокоммуникаций", "Faculty of infocommunications", 4, 2));
                            add(new Faculty(206, "Инженерно-экономический факультет", "Faculty of engineering and economics", 3, 4));
                            add(new Faculty(207, "Факультет инновационного непрерывного образования", "Faculty of innovative lifelong learning", 5, 2));
                            add(new Faculty(208, "Военный факультет", "Military faculty", 5, 4));
                        }
                    }
                }
        };
    }

}
