/*
 * class: EnrolleeDao
 */

package by.epam.admission.dao.impl;

import by.epam.admission.dao.AbstractDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Enrollee;
import by.epam.admission.model.Subject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Maxim Burishinets
 * @version 1.0 1 Sep 2018
 */
public class EnrolleeDao extends AbstractDao<Integer, Enrollee> {

    private static final Logger LOG = LogManager.getLogger(EnrolleeDao.class);

    // SQL queries
    private static final String SQL_SELECT_ENROLLEES;
    private static final String SQL_DELETE_ENROLLEE_BY_ID;
    private static final String SQL_DELETE_ENROLLEE;
    private static final String SQL_INSERT_ENROLLEE;
    private static final String SQL_UPDATE_ENROLLEE;
    private static final String SQL_INSERT_TO_ADMISSION_LIST;
    private static final String SQL_DELETE_FROM_ADMISSION_LIST;
    private static final String SQL_SELECT_ENROLLEE_TOTAL_SCORE;
    private static final String SQL_SELECT_ADMISSION_LIST_ENTRY;
    private static final String SQL_RESTORE_ADMISSION_LIST_ENTRY;
    private static final String SQL_SELECT_ENROLLEE_MARKS;
    private static final String SQL_INSERT_SUBJECT_BY_ENROLLEE_ID;

    // column labels
    private static final String ID = "id";
    private static final String COUNTRY = "country";
    private static final String CITY = "city";
    private static final String SCHOOL_CERTIFICATE = "school_certificate";
    private static final String USER_ID = "users_id";
    private static final String AVAILABLE = "available";
    private static final String SUBJECT_ID = "subjects_id";
    private static final String SCORE = "score";

    private static final int ACTIVE = 1;
    private static final int INACTIVE = 0;

//    @Override
    public List<Enrollee> findAll() throws ProjectException {
        ArrayList<Enrollee> enrollees = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(
                String.format(SQL_SELECT_ENROLLEES, AVAILABLE))) {
            st.setInt(1, 1);
            ResultSet rs = st.executeQuery();
            processResult(enrollees, rs);
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return enrollees;
    }

    @Override
    public Enrollee findEntityById(Integer id) throws ProjectException {
        Enrollee enrollee = null;
        try (PreparedStatement st = connection.prepareStatement(
                String.format(SQL_SELECT_ENROLLEES, ID))) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                enrollee = setEnrollee(rs);
            }
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return enrollee;
    }

    public Enrollee findEnrolleeByUID(int uid) throws ProjectException {
        Enrollee enrollee = null;
        try (PreparedStatement st = connection.prepareStatement(
                String.format(SQL_SELECT_ENROLLEES, USER_ID))) {
            st.setInt(1, uid);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                enrollee = setEnrollee(rs);
            }
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return enrollee;
    }

    public List<Enrollee> findEnrolleesByCountry(String country)
            throws ProjectException {
        ArrayList<Enrollee> enrollees = new ArrayList<>();
        findByAddress(COUNTRY, enrollees, country);
        return enrollees;
    }

    public List<Enrollee> findEnrolleesByCity(String city)
            throws ProjectException {
        ArrayList<Enrollee> enrollees = new ArrayList<>();
        findByAddress(CITY, enrollees, city);
        return enrollees;
    }

    public TreeMap<Subject, Integer> findEnrolleeMarks(int enrolleeId)
            throws ProjectException {
        TreeMap<Subject, Integer> marks = new TreeMap<>();
        try (PreparedStatement st = connection.prepareStatement(
                SQL_SELECT_ENROLLEE_MARKS)) {
            st.setInt(1, enrolleeId);
            st.setInt(2, ACTIVE);
            LOG.debug(enrolleeId + " : " + ACTIVE);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                LOG.debug("START SUBJECT");
                Subject subject = new Subject();
                int subjectId = rs.getInt("id");
                String nameEn = rs.getString("name_en");
                String nameRu = rs.getString("name_ru");
                int score = rs.getInt("score");

                LOG.debug(subjectId + " - " + nameEn + " - " + nameRu + " - " + score);

                subject.setId(subjectId);
                subject.setNameEn(nameEn);
                subject.setNameRu(nameRu);
                marks.put(subject, score);
                LOG.debug("FINISH SUBJECT");
            }
            LOG.debug("Marks: " + marks);
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return marks;
    }

    public boolean checkFaculty(int enrolleeId, int facultyId)
            throws ProjectException {
        boolean result = false;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_SELECT_ADMISSION_LIST_ENTRY)) {
            st.setInt(1, enrolleeId);
            st.setInt(2, facultyId);
            st.setInt(3, ACTIVE);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                result = (rs.getInt(1) == 1);
            }
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return result;
    }

    public boolean checkInactive(int enrolleeId, int facultyId)
            throws ProjectException {
        boolean result = false;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_SELECT_ADMISSION_LIST_ENTRY)) {
            st.setInt(1, enrolleeId);
            st.setInt(2, facultyId);
            st.setInt(3, INACTIVE);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                result = (rs.getInt(1) == 1);
            }
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return result;
    }

    public int findEnrolleeTotalScore(Enrollee enrollee) throws ProjectException {
        int result = -1;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_SELECT_ENROLLEE_TOTAL_SCORE)) {
            st.setInt(1, enrollee.getId());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOG.error("Selection error", e);
            throw new ProjectException("Selection error", e);
        }
        return result;
    }

    // TODO: implemet
    public int findSubjectScoreByEnrolle(Enrollee enrollee, int subjectId) {
        return -1;
    }

    // TODO: find subject by enrollee
    public List<Subject> findSubjectsByEnrollee() {
        return null;
    }

    /**
     * Hard deletion of Enrollee from database, supposed to use only by admin
     * @param id - Enrollee ID to delete from database
     * @return true - if deletion was successful
     * @throws ProjectException - wrapped SQL exception
     */
    @Override
    public boolean delete(Integer id) throws ProjectException {
        int flag;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_DELETE_ENROLLEE_BY_ID)) {
            st.setInt(1, id);
            flag = st.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Deletion error", e);
            throw new ProjectException("Deletion error", e);
        }
        return flag != 0;
    }

    /**
     * Soft deletion of Enrollee from database, supposed to use by client.
     * Sets value `available` of corresponding enrollee to `0`
     * @param enrollee - Enrollee object to delete from database
     * @return true - if deletion was successful
     * @throws ProjectException - wrapped SQL exception
     */
    @Override
    public boolean delete(Enrollee enrollee) throws ProjectException {
        int flag;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_DELETE_ENROLLEE)) {
            st.setInt(1, enrollee.getId());
            flag = st.executeUpdate();
        } catch (SQLException e) {
            throw new ProjectException("Deletion error", e);
        }
        return flag != 0;
    }

    @Override
    public boolean create(Enrollee enrollee) throws ProjectException {
        boolean result;
        try {
            int flag;
            try (PreparedStatement st = connection.prepareStatement(
                    SQL_INSERT_ENROLLEE, Statement.RETURN_GENERATED_KEYS)) {
                st.setString(1, enrollee.getCountry());
                st.setString(2, enrollee.getCity());
                st.setInt(3, enrollee.getSchoolCertificate());
                st.setInt(4, enrollee.getUserId());
                flag = st.executeUpdate();
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    enrollee.setId(rs.getInt(1));
                }
            }
            result = flag != 0;
        } catch (SQLException e) {
            LOG.error("SQL exception", e);
            throw new ProjectException("Insertion error", e);
        }
        return result;
    }

    public boolean addSubject(int enrolleeId, int subjectId, int subjectScore)
            throws ProjectException {
        boolean result = false;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_INSERT_SUBJECT_BY_ENROLLEE_ID)) {
            st.setInt(1, enrolleeId);
            st.setInt(2, subjectId);
            st.setInt(3, subjectScore);
            int rows = st.executeUpdate();
            result = (rows == 1);
        } catch (SQLException e) {
            throw new ProjectException("Subject insertion error", e);
        }
        return result;
    }

    public boolean registerToFacultyById(int enrolleeId, int facultyId)
            throws ProjectException {
        int flag;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_INSERT_TO_ADMISSION_LIST)) {
            st.setInt(1, enrolleeId);
            st.setInt(2, facultyId);
            flag = st.executeUpdate();
        } catch (SQLException e) {
            throw new ProjectException("Updating error", e);
        }
        return flag != 0;
    }

    // TODO: implement faculty deregister
    public boolean deregisterFromFacultyById(int enrolleeId, int facultyId)
            throws ProjectException {
        int flag;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_DELETE_FROM_ADMISSION_LIST)) {
            st.setInt(1, enrolleeId);
            st.setInt(2, facultyId);
            flag = st.executeUpdate();
        } catch (SQLException e) {
            throw new ProjectException("Updating error", e);
        }
        return flag != 0;
    }

    public boolean restoreFacultyRegistration(int enrolleeId, int facultyId)
            throws ProjectException {
        int flag;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_RESTORE_ADMISSION_LIST_ENTRY)) {
            st.setInt(1, enrolleeId);
            st.setInt(2, facultyId);
            flag = st.executeUpdate();
        } catch (SQLException e) {
            throw new ProjectException("Updating error", e);
        }
        return flag != 0;
    }

    @Override
    public Enrollee update(Enrollee enrollee) throws ProjectException {
        int flag;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_UPDATE_ENROLLEE)) {
            st.setString(1, enrollee.getCountry());
            st.setString(2, enrollee.getCity());
            st.setInt(3, enrollee.getSchoolCertificate());
            st.setInt(4,enrollee.getId());
            flag = st.executeUpdate();
        } catch (SQLException e) {
            throw new ProjectException("Updating error", e);
        }
        return flag != 0 ? enrollee : null;
    }

    private void processResult(ArrayList<Enrollee> enrollees, ResultSet rs)
            throws SQLException {
        while (rs.next()) {
            enrollees.add(setEnrollee(rs));
        }
    }

    private Enrollee setEnrollee(ResultSet resultSet) throws SQLException {
        Enrollee enrollee = new Enrollee();
        enrollee.setId(resultSet.getInt(ID));
        enrollee.setCountry(resultSet.getString(COUNTRY));
        enrollee.setCity(resultSet.getString(CITY));
        enrollee.setSchoolCertificate(resultSet.getByte(SCHOOL_CERTIFICATE));
        enrollee.setUserId(resultSet.getInt(USER_ID));
        return enrollee;
    }

    private void findByAddress(String place, ArrayList<Enrollee> enrollees,
                               String value)
            throws ProjectException {
        try (PreparedStatement st = connection.prepareStatement(
                String.format(SQL_SELECT_ENROLLEES, place))) {
            st.setString(1, value);
            ResultSet rs = st.executeQuery();
            processResult(enrollees, rs);
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
    }

    static {
        SQL_SELECT_ENROLLEES =
                "SELECT `id`, " +
                        "`country`, " +
                        "`city`, " +
                        "`school_certificate`, " +
                        "`users_id` " +
                "FROM `enrollees` " +
                "WHERE %s = ?";
        SQL_DELETE_ENROLLEE_BY_ID =
                "DELETE FROM `enrollees` " +
                "WHERE `enrollees`.`id` = ?";
        SQL_DELETE_ENROLLEE =
                "UPDATE `enrollees` " +
                "LEFT JOIN   `enrollees_has_subjects` " +
                "ON `enrollees`.`id` = `enrollees_has_subjects`.`enrollees_id` " +
                "LEFT JOIN   `admission_list` " +
                "ON `enrollees`.`id` = `admission_list`.`enrollees_id`" +
                "SET     `enrollees`.`available` = 0, " +
                        "`enrollees_has_subjects`.`available` = 0, " +
                        "`admission_list`.`available` = 0 " +
                "WHERE  `id` = ? " +
                "AND `enrollees`.`available` = 1";
        SQL_INSERT_ENROLLEE =
                "INSERT INTO `enrollees` " +
                        "(`country`," +
                        "`city`," +
                        "`school_certificate`," +
                        "`users_id`) " +
                "VALUES (?,?,?,?)";
        SQL_UPDATE_ENROLLEE =
                "UPDATE `enrollees` " +
                "SET    `country` = ?," +
                        "`city` = ?, " +
                        "`school_certificate` = ? " +
                "WHERE  `id` = ?";
        SQL_INSERT_TO_ADMISSION_LIST =
                "INSERT INTO `admission_list` " +
                "(`enrollees_id`,`faculties_id`) " +
                "VALUES (?,?)";
        SQL_SELECT_ENROLLEE_TOTAL_SCORE =
                "SELECT (COALESCE(SUM(`enrollees_has_subjects`.`score`), 0) " +
                        "+ `enrollees`.`school_certificate`) AS `total_score` " +
                "FROM `enrollees_has_subjects` " +
                "RIGHT JOIN `enrollees` " +
                "ON `enrollees`.`id` = `enrollees_has_subjects`.`enrollees_id` " +
                "WHERE `enrollees`.`id` = ? " +
                "GROUP BY `enrollees_has_subjects`.`enrollees_id`";
        SQL_SELECT_ADMISSION_LIST_ENTRY =
                "SELECT COUNT(*)" +
                "FROM   `admission_list` " +
                "WHERE  `enrollees_id` = ? " +
                        "AND `faculties_id` = ? " +
                        "AND `available` = ?";
        SQL_DELETE_FROM_ADMISSION_LIST =
                "UPDATE `admission_list` " +
                "SET    `available` = 0 " +
                "WHERE  `enrollees_id` = ? " +
                        "AND `faculties_id` = ?";
        SQL_RESTORE_ADMISSION_LIST_ENTRY =
                "UPDATE `admission_list` " +
                "SET    `available` = 1 " +
                "WHERE  `enrollees_id` = ? " +
                        "AND `faculties_id` = ?";
        SQL_SELECT_ENROLLEE_MARKS =
                "SELECT  `id`, " +
                        "`name_en`, " +
                        "`name_ru`, " +
                        "`score` " +
                "FROM `enrollees_has_subjects` " +
                "JOIN `subjects` " +
                "ON (`subjects`.`id` = `enrollees_has_subjects`.`subjects_id`) " +
                "WHERE `enrollees_id` = ? AND `available` = ?";
        SQL_INSERT_SUBJECT_BY_ENROLLEE_ID =
                "INSERT INTO `enrollees_has_subjects` " +
                "(`enrollees_id`,`subjects_id`,`score`) " +
                "VALUES (?,?,?)";
    }

}
