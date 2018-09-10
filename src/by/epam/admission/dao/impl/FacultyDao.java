/*
 * class: FacultyDao
 */

package by.epam.admission.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import by.epam.admission.dao.AbstractDao;
import by.epam.admission.exception.DaoException;
import by.epam.admission.model.Faculty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Maxim Burishinets
 * @version 1.1 30 Aug 2018
 */
public class FacultyDao extends AbstractDao<Integer, Faculty> {

    private static final Logger LOG = LogManager.getLogger(FacultyDao.class);

    // SQL queries
    private static final String SQL_SELECT_ALL_FACULTIES;
    private static final String SQL_SELECT_FACULTY_BY_ID;
    private static final String SQL_INSERT_FACULTY;
    private static final String SQL_DELETE_FACULTY;
    private static final String SQL_DELETE_FACULTY_BY_ID;
    private static final String SQL_UPDATE_FACULTY;
    private static final String SQL_SELECT_FACULTIES_BY_SUBJECT_ID;

    // column labels
    private static final String ID = "id";
    private static final String NAME_RU = "name_ru";
    private static final String NAME_EN = "name_en";
    private static final String SEATS_PAID = "seats_paid";
    private static final String SEATS_BUDGET = "seats_budget";

    @Override
    public List<Faculty> findAll() {
        ArrayList<Faculty> faculties = new ArrayList<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SQL_SELECT_ALL_FACULTIES);
            processResult(faculties, rs);
        } catch (SQLException e) {
            LOG.error("Selection error", e);

        }
        return faculties;
    }

    @Override
    public Faculty findEntityById(Integer id) {
        Faculty faculty = new Faculty();
        try (PreparedStatement st = connection.prepareStatement(
                SQL_SELECT_FACULTY_BY_ID)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                faculty.setId(id);
                faculty.setNameRu(rs.getString(NAME_RU));
                faculty.setNameEn(rs.getString(NAME_EN));
                faculty.setSeatsPaid(rs.getInt(SEATS_PAID));
                faculty.setSeatsBudget(rs.getInt(SEATS_BUDGET));
            }
        } catch (SQLException e) {
            LOG.error("Selection error", e);
        }
        return faculty;
    }

    public List<Faculty> findFacultiesBySubjectId(int id) {
        ArrayList<Faculty> faculties = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(
                SQL_SELECT_FACULTIES_BY_SUBJECT_ID)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            processResult(faculties, rs);
        } catch (SQLException e) {
            LOG.error("Selection error", e);
        }
        return faculties;
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        int flag;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_DELETE_FACULTY_BY_ID)) {
            flag = st.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Deletion error", e);
            throw new DaoException("Deletion error", e);
        }
        return flag != 0;
    }

    @Override
    public boolean delete(Faculty faculty) throws DaoException {
        try {
            return executeDMLQuery(faculty, SQL_DELETE_FACULTY);
        } catch (SQLException e) {
            LOG.error("Deletion error", e);
            throw new DaoException("Deletion error", e);
        }
    }

    @Override
    public boolean create(Faculty faculty) throws DaoException {
        try {
            return executeDMLQuery(faculty, SQL_INSERT_FACULTY);
        } catch (SQLException e) {
            LOG.error("Insertion error", e);
            throw new DaoException("Insertion error", e);
        }
    }

    @Override
    public Faculty update(Faculty faculty) throws DaoException {
        try {
            boolean result = executeDMLQuery(faculty, SQL_UPDATE_FACULTY);
            return result ? faculty : null;
        } catch (SQLException e) {
            LOG.error("Update error", e);
            throw new DaoException("Update error", e);
        }
    }

    private void processResult(ArrayList<Faculty> faculties, ResultSet rs)
            throws SQLException {
        while (rs.next()) {
            Faculty faculty = new Faculty();
            faculty.setId(rs.getInt(ID));
            faculty.setNameRu(rs.getString(NAME_RU));
            faculty.setNameEn(rs.getString(NAME_EN));
            faculty.setSeatsPaid(rs.getInt(SEATS_PAID));
            faculty.setSeatsBudget(rs.getInt(SEATS_BUDGET));
            faculties.add(faculty);
        }
    }

    private boolean executeDMLQuery(Faculty faculty, String query)
            throws SQLException {
        int flag;
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, faculty.getNameRu());
            st.setString(2, faculty.getNameEn());
            st.setInt(3, faculty.getSeatsPaid());
            st.setInt(4, faculty.getSeatsBudget());
            st.setInt(5, faculty.getId());
            flag = st.executeUpdate();
        }
        return flag != 0;
    }

    static {
        SQL_SELECT_ALL_FACULTIES =
                "SELECT  `id`, " +
                        "`name_ru`, " +
                        "`name_en`, " +
                        "`seats_paid`, " +
                        "`seats_budget` " +
                "FROM `faculties`";
        SQL_SELECT_FACULTY_BY_ID =
                "SELECT  `id`, " +
                        "`name_ru`, " +
                        "`name_en`, " +
                        "`seats_paid`, " +
                        "`seats_budget` " +
                "FROM `faculties` " +
                "WHERE `id` = ?";
        SQL_INSERT_FACULTY =
                "INSERT INTO `faculties` " +
                "(`name_ru`,`name_en`,`seats_paid`,`seats_budget`,`id`) " +
                "VALUES (?,?,?,?,?)";
        SQL_DELETE_FACULTY =
                "DELETE FROM `faculties` " +
                "WHERE  `name_ru` = ? " +
                        "AND `name_en` = ? " +
                        "AND `seats_paid` = ? " +
                        "AND `seats_budget` = ? " +
                        "AND `id` = ?";
        SQL_DELETE_FACULTY_BY_ID =
                "DELETE FROM `faculties` " +
                "WHERE `id` = ?";
        SQL_UPDATE_FACULTY =
                "UPDATE  `faculties` " +
                "SET     `name_ru` = ?, " +
                        "`name_en` = ?, " +
                        "`seats_paid` = ?, " +
                        "`seats_budget` = ? " +
                "WHERE `id` = ?";
        SQL_SELECT_FACULTIES_BY_SUBJECT_ID =
                "SELECT  `faculties`.`id`, " +
                        "`faculties`.`name_ru`, " +
                        "`faculties`.`name_en`, " +
                        "`faculties`.`seats_paid`, " +
                        "`faculties`.`seats_budget` " +
                "FROM `faculties` " +
                "JOIN `faculties_has_subjects` " +
                "ON `faculties`.`id` = `faculties_has_subjects`.`faculties_id` " +
                "WHERE `faculties_has_subjects`.`subjects_id` = ?";
    }
}