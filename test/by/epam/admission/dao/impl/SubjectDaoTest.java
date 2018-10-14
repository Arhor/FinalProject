/*
 * class: SubjectDaoTest
 */

package by.epam.admission.dao.impl;

import by.epam.admission.dao.DaoHelperDbUnit;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Subject;
import by.epam.admission.pool.ConnectionPoolDBUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Maxim Burishinets
 * @version 1.0 30 Aug 2018
 */
public class SubjectDaoTest {

    private static final Logger LOG = LogManager.getLogger(SubjectDaoTest.class);

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

    @Test(dataProvider = "subjectsByFaculties", description = "positive test")
    public void testFindSubjectsByFacultyId(int facultyId, Set<Subject> expected) {
        String failMessage = "Subjects by faculty ID finding test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        SubjectDao sDAO = new SubjectDao();
        try {
            daoHelper.startTransaction(sDAO);
            Set<Subject> actual = sDAO.findSubjectsByFacultyId(facultyId);
            Assert.assertEquals(actual, expected, failMessage);
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "subjectsDataSet", description = "positive test")
    public void testCheckStatus(Subject subject) {
        String failMessage = "Subjects status checking test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        SubjectDao sDAO = new SubjectDao();
        try {
            daoHelper.startTransaction(sDAO);
            boolean result = sDAO.checkStatus(subject.getId());
            Assert.assertTrue(result, failMessage);
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "subjectsDataSet", description = "positive test")
    public void testDelete(Subject subject) {
        String failMessage = "Subject delete test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        SubjectDao sDAO = new SubjectDao();
        try {
            daoHelper.startTransaction(sDAO);
            boolean result = sDAO.delete(subject.getId());
            boolean status = sDAO.checkStatus(subject.getId());
            LOG.info("Deletion result: " + result);
            LOG.info("Subject status: " + status);
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

    @Test(dataProvider = "subjectsToInsert", description = "positive test")
    public void testCreate(Subject subject) {
        String failMessage = "Subject create test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        SubjectDao sDAO = new SubjectDao();
        try {
            daoHelper.startTransaction(sDAO);
            boolean result = sDAO.create(subject);
            Subject inserted = sDAO.findEntityById(subject.getId());
            boolean equals =  subject.equals(inserted);
            LOG.info("Insertion result: " + result);
            LOG.info("Equivalency: " + equals);
            if (!result || !equals) {
                Assert.fail(failMessage);
            }
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.rollback();
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "subjectsDataSet", description = "positive test")
    public void testUpdate(Subject subject) {
        String failMessage = "Subject update test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        SubjectDao sDAO = new SubjectDao();
        try {
            Subject expected = subject.clone();
            daoHelper.startTransaction(sDAO);
            expected.setNameRu("Test_RU");
            expected.setNameEn("Test_EN");
            boolean result = sDAO.update(expected);
            Subject updated = sDAO.findEntityById(expected.getId());
            boolean equals =  expected.equals(updated);
            LOG.info("Insertion result: " + result);
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

    @DataProvider(name = "subjectsDataSet")
    public Object[][] createSelectionData() {
        return new Object[][]{
                {new Subject(101, "Русский язык", "Russian language")},
                {new Subject(102, "Физика", "Physics")},
                {new Subject(103, "Математика", "Math")},
                {new Subject(104, "Химия", "Chemistry")},
                {new Subject(105, "Иностранный язык", "Foreign language")}
        };
    }

    @DataProvider(name = "subjectsToInsert")
    public Object[][] createInsertionData() {
        return new Object[][]{
                {new Subject(106, "Булорусский язык", "Belorussian language")},
                {new Subject(107, "Биология", "Biology")},
                {new Subject(108, "География", "Geography")},
                {new Subject(109, "История", "History")},
                {new Subject(110, "Философия", "Philosophy")}
        };
    }

    @DataProvider(name = "subjectsList")
    public Object[][] createData() {
        return new Object[][] {
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

    @DataProvider(name = "subjectsByFaculties")
    public Object[][] createSubjectsByFaculties() {
        return new Object[][] {
                {201, new TreeSet<Subject>() {
                    {
                        add(new Subject(101, "Русский язык", "Russian language"));
                        add(new Subject(102, "Физика", "Physics"));
                        add(new Subject(103, "Математика", "Math"));
                    }
                }},
                {202, new TreeSet<Subject>() {
                    {
                        add(new Subject(101, "Русский язык", "Russian language"));
                        add(new Subject(102, "Физика", "Physics"));
                        add(new Subject(104, "Химия", "Chemistry"));
                    }
                }},
                {203, new TreeSet<Subject>() {
                    {
                        add(new Subject(103, "Математика", "Math"));
                        add(new Subject(104, "Химия", "Chemistry"));
                        add(new Subject(105, "Иностранный язык", "Foreign language"));
                    }
                }},
                {204, new TreeSet<Subject>() {
                    {
                        add(new Subject(101, "Русский язык", "Russian language"));
                        add(new Subject(103, "Математика", "Math"));
                        add(new Subject(104, "Химия", "Chemistry"));
                    }
                }},
                {205, new TreeSet<Subject>() {
                    {
                        add(new Subject(101, "Русский язык", "Russian language"));
                        add(new Subject(102, "Физика", "Physics"));
                        add(new Subject(103, "Математика", "Math"));
                    }
                }},
                {206, new TreeSet<Subject>() {
                    {
                        add(new Subject(102, "Физика", "Physics"));
                        add(new Subject(103, "Математика", "Math"));
                        add(new Subject(104, "Химия", "Chemistry"));
                    }
                }},
                {207, new TreeSet<Subject>() {
                    {
                        add(new Subject(102, "Физика", "Physics"));
                        add(new Subject(103, "Математика", "Math"));
                        add(new Subject(105, "Иностранный язык", "Foreign language"));
                    }
                }},
                {208, new TreeSet<Subject>() {
                    {
                        add(new Subject(101, "Русский язык", "Russian language"));
                        add(new Subject(103, "Математика", "Math"));
                        add(new Subject(105, "Иностранный язык", "Foreign language"));
                    }
                }}
        };
    }

}