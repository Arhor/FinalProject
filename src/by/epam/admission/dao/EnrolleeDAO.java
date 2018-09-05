/*
 * class: EnrolleeDAO
 */

package by.epam.admission.dao;

import by.epam.admission.exception.DAOException;
import by.epam.admission.exception.NotSupportedOperationException;
import by.epam.admission.model.Enrollee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Maxim Burishinets
 * @version 1.0 1 Sep 2018
 */
public class EnrolleeDAO extends AbstractDAO<Integer, Enrollee> {

    private static final Logger LOG = LogManager.getLogger(EnrolleeDAO.class);

    // SQL queries
    private static final String SQL_SELECT_ENROLLEES;
    private static final String SQL_DELETE_ENROLLEE_BY_ID;
    private static final String SQL_INSERT_ENROLLEE;

    // column labels
    private static final String ID = "id";
    private static final String COUNTRY = "country";
    private static final String CITY = "city";
    private static final String SCHOOL_CERTIFICATE = "school_certificate";
    private static final String IS_PASSED = "is_passed";
    private static final String USER_ID = "users_id";
    private static final String FACULTY_ID = "faculties_id";
    private static final String AVAILABLE = "available";

    @Override
    public List<Enrollee> findAll() {
        ArrayList<Enrollee> enrollees = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(
                String.format(SQL_SELECT_ENROLLEES, AVAILABLE))) {
            st.setInt(1, 1);
            ResultSet rs = st.executeQuery();
            processResult(enrollees, rs);
        } catch (SQLException e) {
            LOG.error("Selection error", e);
        }
        return enrollees;
    }

    @Override
    public Enrollee findEntityById(Integer id) {
        Enrollee enrollee = null;
        try (PreparedStatement st = connection.prepareStatement(
                String.format(SQL_SELECT_ENROLLEES, ID))) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                enrollee = setEnrolle(rs);
            }
        } catch (SQLException e) {
            LOG.error("Selection error", e);
        }
        return enrollee;
    }

    public List<Enrollee> findEnrolleesByCountry(String country) {
        ArrayList<Enrollee> enrollees = new ArrayList<>();
        findByAddress(COUNTRY, enrollees, country);
        return enrollees;
    }

    public List<Enrollee> findEnrolleesByCity(String city) {
        ArrayList<Enrollee> enrollees = new ArrayList<>();
        findByAddress(CITY, enrollees, city);
        return enrollees;
    }

    private void findByAddress(String place, ArrayList<Enrollee> enrollees, String value) {
        try (PreparedStatement st = connection.prepareStatement(
                String.format(SQL_SELECT_ENROLLEES, place))) {
            st.setString(1, value);
            ResultSet rs = st.executeQuery();
            processResult(enrollees, rs);
        } catch (SQLException e) {
            LOG.error("Selection error", e);
        }
    }

    @Override
    public boolean delete(Integer id) throws DAOException {
        int flag = 0;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_DELETE_ENROLLEE_BY_ID)) {
            st.setInt(1, id);
            flag = st.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Deletion error", e);
            throw new DAOException("Deletion error", e);
        }
        return flag != 0;
    }

    @Override
    public boolean delete(Enrollee enrollee) throws NotSupportedOperationException {
        throw new NotSupportedOperationException();
    }

    @Override
    public boolean create(Enrollee enrollee) throws DAOException {
        boolean result = false;
        try {
            int flag = 0;
            try (PreparedStatement st = connection.prepareStatement(
                    SQL_INSERT_ENROLLEE, Statement.RETURN_GENERATED_KEYS)) {
                st.setString(1, enrollee.getCountry());
                st.setString(2, enrollee.getCity());
                st.setInt(3, enrollee.getSchoolCertificate());
                st.setInt(4, enrollee.getUserId());
                st.setInt(5,enrollee.getFacultyId());
                flag = st.executeUpdate();
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    enrollee.setId(rs.getInt(1));
                }
            }
            result = flag != 0;
        } catch (SQLException e) {
            LOG.error("SQL exception", e);
            throw new DAOException("Insertion error", e);
        }
        return result;
    }

    @Override
    public Enrollee update(Enrollee enrollee) {
        return null;
    }

    private void processResult(ArrayList<Enrollee> enrollees, ResultSet rs)
            throws SQLException {
        while (rs.next()) {
            enrollees.add(setEnrolle(rs));
        }
    }

    private boolean executeDMLQuery(Enrollee enrollee, String query)
            throws SQLException {
        int flag = 0;
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, enrollee.getCountry());
            st.setString(2, enrollee.getCity());
            st.setInt(3, enrollee.getSchoolCertificate());
            st.setInt(4, enrollee.getUserId());
            st.setInt(5,enrollee.getFacultyId());
            flag = st.executeUpdate();
        }
        return flag != 0;
    }

    private Enrollee setEnrolle(ResultSet resultSet) throws SQLException {
        Enrollee enrollee = new Enrollee();
        enrollee.setId(resultSet.getInt(ID));
        enrollee.setCountry(resultSet.getString(COUNTRY));
        enrollee.setCity(resultSet.getString(CITY));
        enrollee.setSchoolCertificate(resultSet.getByte(SCHOOL_CERTIFICATE));
        enrollee.setPassed(resultSet.getByte(IS_PASSED) == 1);
        enrollee.setUserId(resultSet.getInt(USER_ID));
        enrollee.setFacultyId(resultSet.getInt(FACULTY_ID));
        return enrollee;
    }

    static {
        SQL_SELECT_ENROLLEES =
                "SELECT `id`, " +
                        "`country`, " +
                        "`city`, " +
                        "`school_certificate`, " +
                        "`is_passed`, " +
                        "`users_id`, " +
                        "`faculties_id` " +
                "FROM `enrollees` " +
                "WHERE %s = ?";
        SQL_DELETE_ENROLLEE_BY_ID =
                "DELETE FROM `enrollees` " +
                "WHERE `enrollees`.`id` = ?";
        SQL_INSERT_ENROLLEE =
                "INSERT INTO `enrollees` " +
                "(`country`,`city`,`school_certificate`,`users_id`,`faculties_id`,`available`) " +
                "VALUES (?,?,?,?,?,1)"; // 1 - means account is available
    }

}
