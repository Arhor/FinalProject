/*
 * class: EnrolleeDaoTest
 */

package by.epam.admission.dao.impl;

import by.epam.admission.dao.DaoHelperDbUnit;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Enrollee;
import by.epam.admission.model.Subject;
import by.epam.admission.model.User;
import by.epam.admission.pool.ConnectionPoolDBUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.util.*;

/**
 * @author Burishinets Maxim
 * @version 1.0 29 Aug 2018
 */
public class EnrolleeDaoTest {

    private static final Logger LOG = LogManager.getLogger(
            EnrolleeDaoTest.class);

    private static ConnectionPoolDBUnit pool = ConnectionPoolDBUnit.POOL;

    @Test(dataProvider = "enrolleesList", priority =  2, description = "positive test")
    public void testFindAll(List<Enrollee> expected) {
        String failMessage = "";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        EnrolleeDao eDAO = new EnrolleeDao();
        try {
            daoHelper.startTransaction(eDAO);
            int pageNumber = 0;
            int rowsPerPage = 10;
            List<Enrollee> actual = eDAO.findAll(pageNumber, rowsPerPage);
            Assert.assertEquals(actual, expected, failMessage);
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "enrolleesDataSet", priority =  2, description = "positive test")
    public void testFindEntityById(Enrollee expected) {
        String failMessage = "Enrollee finding test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        EnrolleeDao eDAO = new EnrolleeDao();
        try {
            daoHelper.startTransaction(eDAO);
            Enrollee actual = eDAO.findEntityById(expected.getId());
            Assert.assertEquals(actual, expected, failMessage);
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "enrolleesDataSet", priority =  2, description = "positive test")
    public void testFindEnrolleeByUserId(Enrollee expected) {
        String failMessage = "Enrollee finding test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        EnrolleeDao eDAO = new EnrolleeDao();
        try {
            daoHelper.startTransaction(eDAO);
            Enrollee actual = eDAO.findEnrolleeByUserId(expected.getUserId());
            Assert.assertEquals(actual, expected, failMessage);
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "enrolleesByFacultyCount", priority =  2, description = "positive test")
    public void testFindEnrolleesByFacultyId(int facultyId, int expected ) {
        String failMessage = "Enrollees by faculties finding test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        EnrolleeDao eDAO = new EnrolleeDao();
        try {
            daoHelper.startTransaction(eDAO);
            List<Enrollee> enrollees = eDAO.findEnrolleesByFacultyId(facultyId);
            int actual = enrollees.size();
            Assert.assertEquals(actual, expected, failMessage);
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "enrolleesByFaculty", priority =  2, description = "positive test")
    public void testFindEnrolleeStatus(Integer facultyId, List<Enrollee> enrollees) {
        String failMessage = "Admission list status finding test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        EnrolleeDao eDAO = new EnrolleeDao();
        try {
            daoHelper.startTransaction(eDAO);
            String expected = "none";
            for (Enrollee enrollee : enrollees) {
                String actual = eDAO.findEnrolleeStatus(enrollee.getId(), facultyId);
                LOG.info(actual + " : " + expected);
                Assert.assertEquals(actual, expected, failMessage);
            }
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "EnrolleeMarks", priority =  2, description = "positive test")
    public void testFindEnrolleeMarks(Integer enrolleeId, TreeMap<Subject, Integer> expected) {
        String failMessage = "Enrollee marks finding test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        EnrolleeDao eDAO = new EnrolleeDao();
        try {
            daoHelper.startTransaction(eDAO);
            TreeMap<Subject, Integer> actual = eDAO.findEnrolleeMarks(enrolleeId);
            Assert.assertEquals(actual, expected, failMessage);
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "enrolleesDataSet", priority =  2, description = "positive test")
    public void testCheckStatus(Enrollee enrollee) {
        String failMessage = "Enrollee status finding test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        EnrolleeDao eDAO = new EnrolleeDao();
        try {
            daoHelper.startTransaction(eDAO);
            boolean status = eDAO.checkStatus(enrollee.getId());
            Assert.assertTrue(status);
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "enrolleesByFaculty", priority =  2, description = "positive test")
    public void testCheckFaculty(Integer facultyId, List<Enrollee> enrollees) {
        String failMessage = "Enrollee active faculty registration status finding test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        EnrolleeDao eDAO = new EnrolleeDao();
        try {
            daoHelper.startTransaction(eDAO);
            for (Enrollee enrollee : enrollees) {
                boolean status = eDAO.checkFaculty(enrollee.getId(), facultyId);
                Assert.assertTrue(status, failMessage);
            }
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "enrolleesByFaculty", priority =  2, description = "positive test")
    public void testCheckAdmissionListStatus(Integer facultyId, List<Enrollee> enrollees) {
        String failMessage = "Enrollee faculty registration status finding test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        EnrolleeDao eDAO = new EnrolleeDao();
        try {
            daoHelper.startTransaction(eDAO);
            for (Enrollee enrollee : enrollees) {
                boolean status = eDAO.checkAdmissionListStatus(enrollee.getId(), facultyId);
                Assert.assertTrue(status, failMessage);
            }
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "enrolleesDataSet", priority =  2, description = "positive test")
    public void testDelete(Enrollee enrollee) {
        String failMessage = "Enrollee delete test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        EnrolleeDao eDAO = new EnrolleeDao();
        try {
            daoHelper.startTransaction(eDAO);
            boolean result = eDAO.delete(enrollee.getId());
            boolean status = eDAO.checkStatus(enrollee.getId());
            if (!result || status) {
                LOG.info("Deletion result: " + result);
                LOG.info("Enrollee status: " + status);
                Assert.fail(failMessage);
            }
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.rollback();
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "enrolleesToInsert", priority =  1, description = "positive test")
    public void testCreate(User user, Enrollee enrollee) {
        String failMessage = "Enrollee creation test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        UserDao uDAO = new UserDao();
        EnrolleeDao eDAO = new EnrolleeDao();
        try {
            daoHelper.startTransaction(uDAO, eDAO);
            user.setPassword(user.getEmail());
            uDAO.create(user);
            enrollee.setUserId(user.getId());
            boolean result = eDAO.create(enrollee);
            Enrollee inserted = eDAO.findEntityById(enrollee.getId());
            boolean equals = enrollee.equals(inserted);
            if (!result || !equals) {
                LOG.info("Creation result: " + result);
                LOG.info("Equivalency: " + equals);
                Assert.fail(failMessage);
            } else {
                daoHelper.commit();
            }
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
            daoHelper.endTransaction();
        }
    }

    @Test(dataProvider = "enrolleesToInsert", priority =  99, description = "positive test")
    public void testAddSubject(User user, Enrollee enrollee) {
        String failMessage = "Enrollee add subject test failed";
        DaoHelperDbUnit daoHelper = new DaoHelperDbUnit();
        EnrolleeDao eDAO = new EnrolleeDao();
        try {
            daoHelper.startTransaction(eDAO);
            Subject subject = new Subject(103, "Математика", "Math");
            int score = 99;
            boolean result = eDAO.addSubject(enrollee.getId(), subject.getId(), score);
            TreeMap<Subject, Integer> marks = eDAO.findEnrolleeMarks(enrollee.getId());
            boolean equals = (marks.get(subject) == score);
            if (!result || !equals) {
                Assert.fail(failMessage);
            }
        } catch (ProjectException e) {
            Assert.fail(failMessage, e);
        } finally {
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

    @DataProvider(name = "enrolleesDataSet")
    public Object[][] createSelectionData() {
        return new Object[][] {
                {new Enrollee(1, "Россия", "Екатеринбург", 9, 2)},
                {new Enrollee(2, "USA", "Los Angeles", 61, 3)},
                {new Enrollee(3, "Россия", "Екатеринбург", 76, 4)},
                {new Enrollee(4, "Беларусь", "Брест", 53, 5)},
                {new Enrollee(5, "France", "Paris", 79, 6)},
                {new Enrollee(6, "USA", "Houston", 76, 7)},
                {new Enrollee(7, "Germany", "Munich", 86, 8)},
                {new Enrollee(8, "Россия", "Калининград", 70, 9)},
                {new Enrollee(9, "Germany", "Munich", 96, 10)},
                {new Enrollee(10, "Беларусь", "Брест", 69, 11)}
        };
    }

    @DataProvider(name = "enrolleesToInsert")
    public Object[][] createInsertionData() {
        return new Object[][] {
                {
                    new User("MaxFactor", "Brown", "example.101@gmail.com", User.Lang.EN, User.Role.CLIENT),
                    new Enrollee("Россия", "Екатеринбург", 9)
                },
                {
                    new User("Тимофей", "Королёв", "example.102@gmail.com", User.Lang.RU, User.Role.CLIENT),
                    new Enrollee("USA", "Los Angeles", 61)
                },
                {
                    new User("Archie", "Edwards", "example.103@gmail.com", User.Lang.EN, User.Role.CLIENT),
                    new Enrollee("Россия", "Екатеринбург", 76)
                },
                {
                    new User("Антон", "Устинов", "example.104@gmail.com", User.Lang.RU, User.Role.CLIENT),
                    new Enrollee("Беларусь", "Минск", 53)
                },
                {
                    new User("Георгий", "Королёв", "example.105@gmail.com", User.Lang.RU, User.Role.CLIENT),
                    new Enrollee("France", "Paris", 79)
                }
        };
    }

    @DataProvider(name = "enrolleesList")
    public Object[][] createData() {
        return new Object[][] {
                {
                    new ArrayList<Enrollee>(){
                        {
                            add(new Enrollee(1, "Россия", "Екатеринбург", 9, 2));
                            add(new Enrollee(2, "USA", "Los Angeles", 61, 3));
                            add(new Enrollee(3, "Россия", "Екатеринбург", 76, 4));
                            add(new Enrollee(4, "Беларусь", "Брест", 53, 5));
                            add(new Enrollee(5, "France", "Paris", 79, 6));
                            add(new Enrollee(6, "USA", "Houston", 76, 7));
                            add(new Enrollee(7, "Germany", "Munich", 86, 8));
                            add(new Enrollee(8, "Россия", "Калининград", 70, 9));
                            add(new Enrollee(9, "Germany", "Munich", 96, 10));
                            add(new Enrollee(10, "Беларусь", "Брест", 69, 11));
                        }
                    }
                }
        };
    }

    @DataProvider(name = "enrolleesByFaculty")
    public Object[][] createEnrolleesByFaculties() {
        return new Object[][] {
                {201, new ArrayList<Enrollee>(){
                    {
                        add(new Enrollee(10, "Беларусь", "Брест", 69, 11));
                        add(new Enrollee(16, "Беларусь", "Брест", 11, 17));
                        add(new Enrollee(24, "Germany", "Cologne", 65, 25));
                    }
                }},
                {202, new ArrayList<Enrollee>(){
                    {
                        add(new Enrollee(28, "Россия", "Калининград", 76, 29));
                    }
                }},
                {203, new ArrayList<Enrollee>(){
                    {
                        add(new Enrollee(6, "USA", "Houston", 76, 7));
                        add(new Enrollee(22, "France", "Marseille", 13, 23));
                        add(new Enrollee(29, "Беларусь", "Витебск", 51, 30));
                    }
                }},
                {204, new ArrayList<Enrollee>(){
                    {
                        add(new Enrollee(2, "USA", "Los Angeles", 61, 3));
                        add(new Enrollee(9, "Germany", "Munich", 96, 10));
                        add(new Enrollee(11, "Беларусь", "Брест", 56, 12));
                        add(new Enrollee(23, "Беларусь", "Гродно", 66, 24));
                        add(new Enrollee(27, "Россия", "Москва", 83, 28));
                    }
                }},
                {205, new ArrayList<Enrollee>(){
                    {
                        add(new Enrollee(21, "Беларусь", "Минск", 58, 22));
                        add(new Enrollee(25, "Germany", "Munich", 84, 26));
                    }
                }},
                {206, new ArrayList<Enrollee>(){
                    {
                        add(new Enrollee(3, "Россия", "Екатеринбург", 76, 4));
                        add(new Enrollee(12, "Россия", "Ростов", 30, 13));
                        add(new Enrollee(19, "Россия", "Ростов", 17, 20));
                    }
                }},
                {207, new ArrayList<Enrollee>(){
                    {
                        add(new Enrollee(8, "Россия", "Калининград", 70, 9));
                        add(new Enrollee(14, "Беларусь", "Витебск", 57, 15));
                        add(new Enrollee(20, "Беларусь", "Гродно", 99, 21));
                        add(new Enrollee(26, "Россия", "Ростов", 10, 27));
                    }
                }},
                {208, new ArrayList<Enrollee>(){
                    {
                        add(new Enrollee(7, "Germany", "Munich", 86, 8));
                        add(new Enrollee(13, "Беларусь", "Минск", 14, 14));
                        add(new Enrollee(18, "Россия", "Москва", 78, 19));
                    }
                }},
        };
    }

    @DataProvider(name = "enrolleesByFacultyCount")
    public Object[][] createEnrolleesByFacultiesCount() {
        return new Object[][] {
                {201, 8},
                {202, 7},
                {203, 7},
                {204, 9},
                {205, 7},
                {206, 7},
                {207, 7},
                {208, 6},
        };
    }

    @DataProvider(name = "EnrolleeMarks")
    public Object[][] createSubjectsByFaculties() {
        return new Object[][] {
                {1, new TreeMap<Subject, Integer>() {
                    {
                        put(new Subject(101, "Русский язык", "Russian language"), 71);
                        put(new Subject(102, "Физика", "Physics"), 77);
                        put(new Subject(103, "Математика", "Math"), 48);
                    }
                }},
                {2, new TreeMap<Subject, Integer>() {
                    {
                        put(new Subject(101, "Русский язык", "Russian language"), 51);
                        put(new Subject(103, "Математика", "Math"), 47);
                        put(new Subject(104, "Химия", "Chemistry"), 71);
                    }
                }},
                {3, new TreeMap<Subject, Integer>() {
                    {
                        put(new Subject(102, "Физика", "Physics"), 29);
                        put(new Subject(103, "Математика", "Math"), 48);
                        put(new Subject(104, "Химия", "Chemistry"), 49);
                    }
                }},
                {4, new TreeMap<Subject, Integer>() {
                    {
                        put(new Subject(101, "Русский язык", "Russian language"), 79);
                        put(new Subject(102, "Физика", "Physics"), 30);
                        put(new Subject(104, "Химия", "Chemistry"), 90);
                    }
                }},
                {5, new TreeMap<Subject, Integer>() {
                    {
                        put(new Subject(101, "Русский язык", "Russian language"), 93);
                        put(new Subject(102, "Физика", "Physics"), 89);
                        put(new Subject(103, "Математика", "Math"), 68);
                    }
                }}
        };
    }

}