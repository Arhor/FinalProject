/*
 * class: SubjectDaoTest
 */

package by.epam.admission.dao.impl;

import by.epam.admission.dao.DaoHelperDbUnit;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Subject;
import by.epam.admission.pool.ConnectionPoolDBUnit;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Maxim Burishinets
 * @version 1.0 30 Aug 2018
 */
public class SubjectDaoTest {

    private static ConnectionPoolDBUnit pool = ConnectionPoolDBUnit.POOL;

    @Test(dataProvider = "subjectsList", description = "positive test")
    public void testFindAll(List<Subject> expected) {
        String failMessage = "Subject finding all test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        SubjectDao sDAO = new SubjectDao();
        try {
            daoHelper.startTransaction(sDAO);
            List<Subject> actual = sDAO.findAll();
            Assert.assertEquals(actual, expected, failMessage);
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "subjectsDataSet", description = "positive test")
    public void testFindEntityById(Subject expected) {
        String failMessage = "Subject finding test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        SubjectDao sDAO = new SubjectDao();
        try {
            daoHelper.startTransaction(sDAO);
            Subject actual = sDAO.findEntityById(expected.getId());
            Assert.assertEquals(actual, expected, failMessage);
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test
    public void testFindSubjectsByFacultyId() {
    }

    @Test
    public void testDelete() {
    }

    @Test
    public void testCreate() {
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

    @DataProvider(name = "subjectsDataSet")
    public Object[][] createDataSet() {
        return new Object[][]{
                {new Subject(101, "Русский язык", "Russian language")},
                {new Subject(102, "Физика", "Physics")},
                {new Subject(103, "Математика", "Math")},
                {new Subject(104, "Химия", "Chemistry")},
                {new Subject(105, "Иностранный язык", "Foreign language")}
        };
    }

    @DataProvider(name = "subjectsList")
    public Object[][] createData() {
        return new Object[][]{
                {
                    new ArrayList<Subject>() {
                        {
                            add(new Subject(101, "Русский язык", "Russian language"));
                            add(new Subject(102, "Физика", "Physics"));
                            add(new Subject(103, "Математика", "Math"));
                            add(new Subject(104, "Химия", "Chemistry"));
                            add(new Subject(105, "Иностранный язык", "Foreign language"));
                        }
                    }
                }
        };
    }

}