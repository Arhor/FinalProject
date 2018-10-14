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
import java.util.TreeMap;

/**
 * @author Maxim Burishinets
 * @version 1.0 1 Sep 2018
 */
public class EnrolleeDao extends AbstractDao<Integer, Enrollee> {

    private static final Logger LOG = LogManager.getLogger(EnrolleeDao.class);

    // SQL queries
    private static final String SQL_SELECT_ENROLLEES_PAGINATION;
    private static final String SQL_SELECT_ENROLLEES_TOTAL_AMOUNT;

    private static final String SQL_SELECT_ENROLLEES;
    private static final String SQL_DELETE_ENROLLEE;
    private static final String SQL_INSERT_ENROLLEE;
    private static final String SQL_UPDATE_ENROLLEE;
    private static final String SQL_INSERT_TO_ADMISSION_LIST;
    private static final String SQL_DELETE_FROM_ADMISSION_LIST;
    private static final String SQL_SELECT_ADMISSION_LIST_ENTRY;
    private static final String SQL_RESTORE_ADMISSION_LIST_ENTRY;
    private static final String SQL_SELECT_ENROLLEE_MARKS;
    private static final String SQL_INSERT_SUBJECT_BY_ENROLLEE_ID;
    private static final String SQL_CHECK_ADMISSION_LIST_STATUS;
    private static final String SQL_SELECT_BEST_ENROLLEES_IDS;
    private static final String SQL_UPDATE_ADMISSION_LIST_STATUS;
    private static final String SQL_SELECT_ENROLLEES_BY_FACULTY_ID;
    private static final String SQL_SELECT_ENROLLEE_STATUS;
    private static final String SQL_CHECK_ENROLLEE_STATUS;

    // column labels
    private static final String ID = "id";
    private static final String COUNTRY = "country";
    private static final String CITY = "city";
    private static final String SCHOOL_CERTIFICATE = "school_certificate";
    private static final String USER_ID = "users_id";
    private static final String AVAILABLE = "available";
    private static final String IS_PASSED = "is_passed";

    private static final int ACTIVE = 1;

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




    public List<Enrollee> findAll(int pageNumber, int rowsPerPager)
            throws ProjectException {
        ArrayList<Enrollee> enrollees = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(
                SQL_SELECT_ENROLLEES_PAGINATION)) {
            int leftBorder = pageNumber * rowsPerPager + 1;
            int rightBorder = rowsPerPager * (pageNumber + 1);
            st.setInt(1,leftBorder);
            st.setInt(2, rightBorder);
            ResultSet rs = st.executeQuery();
            processResult(enrollees, rs);
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return enrollees;
    }

    public int findTotalAmount() throws ProjectException {
        int totalAmount = 0;
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SQL_SELECT_ENROLLEES_TOTAL_AMOUNT);
            if (rs.next()) {
                totalAmount += rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return totalAmount;
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

    public Enrollee findEnrolleeByUserId(int UserId) throws ProjectException {
        Enrollee enrollee = null;
        try (PreparedStatement st = connection.prepareStatement(
                String.format(SQL_SELECT_ENROLLEES, USER_ID))) {
            st.setInt(1, UserId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                enrollee = setEnrollee(rs);
                LOG.debug(enrollee);
            }
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return enrollee;
    }

    public List<Enrollee> findEnrolleesByFacultyId(int facultyId)
            throws ProjectException {
        ArrayList<Enrollee> enrollees = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(
                SQL_SELECT_ENROLLEES_BY_FACULTY_ID)) {
            st.setInt(1, facultyId);
            st.setInt(2 , ACTIVE);
            ResultSet rs = st.executeQuery();
            processResult(enrollees, rs);
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return enrollees;
    }

    public String findEnrolleeStatus(int enrolleeId, int facultyId)
            throws ProjectException {
        String result = "";
        try (PreparedStatement st = connection.prepareStatement(
                SQL_SELECT_ENROLLEE_STATUS)) {
            st.setInt(1, enrolleeId);
            st.setInt(2, facultyId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                result = rs.getString(IS_PASSED);
            }
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return result;
    }

    public ArrayList<Integer> findBestEnrolleesIds(int facultyId, int offset, int limit) throws ProjectException {
        ArrayList<Integer> ids = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(SQL_SELECT_BEST_ENROLLEES_IDS)) {
            st.setInt(1, facultyId);
            st.setInt(2, limit);
            st.setInt(3, offset);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return ids;
    }

    public TreeMap<Subject, Integer> findEnrolleeMarks(int enrolleeId)
            throws ProjectException {
        TreeMap<Subject, Integer> marks = new TreeMap<>();
        try (PreparedStatement st = connection.prepareStatement(
                SQL_SELECT_ENROLLEE_MARKS)) {
            st.setInt(1, enrolleeId);
            st.setInt(2, ACTIVE);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Subject subject = new Subject();
                int subjectId = rs.getInt("id");
                String nameEn = rs.getString("name_en");
                String nameRu = rs.getString("name_ru");
                int score = rs.getInt("score");
                subject.setId(subjectId);
                subject.setNameEn(nameEn);
                subject.setNameRu(nameRu);
                marks.put(subject, score);
            }
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return marks;
    }

    @Override
    public boolean checkStatus(Integer enrolleeId) throws ProjectException {
        boolean result = false;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_CHECK_ENROLLEE_STATUS)) {
            st.setInt(1, enrolleeId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                result = (rs.getInt(AVAILABLE) == 1);
            }
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return result;
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

    public boolean checkAdmissionListStatus(int enrolleeId, int facultyId)
            throws ProjectException {
        boolean result = false;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_CHECK_ADMISSION_LIST_STATUS)) {
            st.setInt(1, enrolleeId);
            st.setInt(2, facultyId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                result = (rs.getInt(1) == 1);
            }
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return result;
    }

    /**
     * Soft deletion of Enrollee from database, supposed to use by client.
     * Sets value `available` of corresponding enrollee to `0`
     * @param id - Enrollee ID to delete from database
     * @return true - if deletion was successful
     * @throws ProjectException - wrapped SQL exception
     */
    @Override
    public boolean delete(Integer id) throws ProjectException {
        int flag;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_DELETE_ENROLLEE)) {
            st.setInt(1, id);
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
            throw new ProjectException("Insertion error", e);
        }
        return result;
    }

    public boolean addSubject(int enrolleeId, int subjectId, int subjectScore)
            throws ProjectException {
        boolean result;
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
        int flag = processFacultyRegistration(enrolleeId, facultyId,
                SQL_INSERT_TO_ADMISSION_LIST);
        return flag != 0;
    }

    public boolean deregisterFromFacultyById(int enrolleeId, int facultyId)
            throws ProjectException {
        int flag = processFacultyRegistration(enrolleeId, facultyId,
                SQL_DELETE_FROM_ADMISSION_LIST);
        return flag != 0;
    }

    public boolean restoreFacultyRegistration(int enrolleeId, int facultyId)
            throws ProjectException {
        int flag = processFacultyRegistration(enrolleeId, facultyId,
                SQL_RESTORE_ADMISSION_LIST_ENTRY);
        return flag != 0;
    }

    public boolean updateAdmissionList(int facultyId,
                                       ArrayList<Integer> enrolleeIds,
                                       Seats seats)
            throws ProjectException {
        boolean result;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < enrolleeIds.size(); i++) {
            if (i == 0) { sb.append("("); }
            sb.append(enrolleeIds.get(i));
            if (i == enrolleeIds.size() - 1) {
                sb.append(")");
            } else {
                sb.append(",");
            }
        }
        try (PreparedStatement st = connection.prepareStatement(
                String.format(SQL_UPDATE_ADMISSION_LIST_STATUS, sb.toString()))) {
            st.setString(1, seats.toString().toLowerCase());
            st.setInt(2, facultyId);
            int rows = st.executeUpdate();
            result = (rows == enrolleeIds.size());
        } catch (SQLException e) {
            throw new ProjectException("Update error", e);
        }
        return result;
    }

    @Override
    public boolean update(Enrollee enrollee) throws ProjectException {
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
        return flag != 0;
    }

    private int processFacultyRegistration(int enrolleeId,
                                           int facultyId,
                                           String sql)
            throws ProjectException {
        int flag;
        try (PreparedStatement st = connection.prepareStatement(
                sql)) {
            st.setInt(1, enrolleeId);
            st.setInt(2, facultyId);
            flag = st.executeUpdate();
        } catch (SQLException e) {
            throw new ProjectException("Updating error", e);
        }
        return flag;
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

    public enum Seats {
        BUDGET, PAID
    }

    static {
        SQL_SELECT_ENROLLEES_BY_FACULTY_ID =
                "SELECT  enrollees.id, " +
                        "enrollees.country, " +
                        "enrollees.city, " +
                        "enrollees.school_certificate, " +
                        "enrollees.users_id " +
                "FROM enrollees " +
                "JOIN admission_list " +
                "ON enrollees.id = admission_list.enrollees_id " +
                "WHERE   admission_list.faculties_id = ? " +
                        "AND admission_list.available = ?";
        SQL_SELECT_ENROLLEE_STATUS =
                "SELECT `is_passed` " +
                "FROM `admission_list` " +
                "WHERE `enrollees_id` = ? AND `faculties_id` = ?";
        SQL_DELETE_ENROLLEE =
                "UPDATE `enrollees` " +
                "SET    `available` = 0 " +
                "WHERE  `id` = ? ";
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
        SQL_SELECT_ADMISSION_LIST_ENTRY =
                "SELECT COUNT(*)" +
                "FROM   `admission_list` " +
                "WHERE  `enrollees_id` = ? " +
                        "AND `faculties_id` = ? " +
                        "AND `available` = ?";
        SQL_CHECK_ADMISSION_LIST_STATUS =
                "SELECT COUNT(*)" +
                "FROM   `admission_list` " +
                "WHERE  `enrollees_id` = ? " +
                        "AND `faculties_id` = ? ";
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
                "SELECT  `subjects`.`id`, " +
                        "`subjects`.`name_en`, " +
                        "`subjects`.`name_ru`, " +
                        "`enrollees_has_subjects`.`score` " +
                "FROM `enrollees_has_subjects` " +
                "JOIN `subjects` " +
                "ON (`subjects`.`id` = `enrollees_has_subjects`.`subjects_id`) " +
                "WHERE `enrollees_id` = ? AND `enrollees_has_subjects`.`available` = ?";
        SQL_INSERT_SUBJECT_BY_ENROLLEE_ID =
                "INSERT INTO `enrollees_has_subjects` " +
                "(`enrollees_id`,`subjects_id`,`score`) " +
                "VALUES (?,?,?)";
        SQL_SELECT_BEST_ENROLLEES_IDS =
                "SELECT `enrollees`.`id` " +
                "FROM   `enrollees` " +
                "JOIN   `admission_list` ON `enrollees`.`id` = `admission_list`.`enrollees_id` " +
                "JOIN   `faculties` ON `faculties`.`id` = `admission_list`.`faculties_id` " +
                "JOIN ( SELECT `enrollees_has_subjects`.`enrollees_id`, " +
                "              sum(`score`) AS `score` " +
                "       FROM `enrollees_has_subjects` " +
                "       GROUP BY `enrollees_has_subjects`.`enrollees_id` " +
                ") AS `total_score` ON `enrollees`.`id` = `total_score`.`enrollees_id` " +
                "WHERE `faculties`.`id` = ? AND `admission_list`.`available` = 1 " +
                "ORDER BY (`total_score`.`score` + `enrollees`.`school_certificate`) " +
                "DESC LIMIT ? OFFSET ?";
        SQL_UPDATE_ADMISSION_LIST_STATUS =
                "UPDATE `admission_list` " +
                "SET `is_passed` = ? " +
                "WHERE `enrollees_id` IN %s AND `faculties_id` = ?";
        SQL_CHECK_ENROLLEE_STATUS =
                "SELECT `available` " +
                "FROM `enrollees` " +
                "WHERE `id` = ?";
        SQL_SELECT_ENROLLEES =
                "SELECT `id`, " +
                        "`country`, " +
                        "`city`, " +
                        "`school_certificate`, " +
                        "`users_id` " +
                        "FROM `enrollees` " +
                        "WHERE %s = ?";
        SQL_SELECT_ENROLLEES_PAGINATION =
                "SELECT  `id`, " +
                        "`country`, " +
                        "`city`, " +
                        "`school_certificate`, " +
                        "`users_id` " +
                "FROM   `enrollees` " +
                "WHERE  `id` >= ? " +
                        "AND `id` <= ?";
        SQL_SELECT_ENROLLEES_TOTAL_AMOUNT =
                "SELECT COUNT(*) " +
                "FROM `enrollees`";
    }

}
