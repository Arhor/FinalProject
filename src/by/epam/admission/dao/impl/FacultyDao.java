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
import by.epam.admission.exception.ProjectException;
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
    private static final String SQL_UPDATE_FACULTY;
    private static final String SQL_CHECK_FACULTY_STATUS;

    // column labels
    private static final String ID = "id";
    private static final String NAME_RU = "name_ru";
    private static final String NAME_EN = "name_en";
    private static final String SEATS_PAID = "seats_paid";
    private static final String SEATS_BUDGET = "seats_budget";
    private static final String AVAILABLE = "available";
    private static final String CHECKED = "checked";

//    @Override
    public List<Faculty> findAll() throws ProjectException {
        ArrayList<Faculty> faculties = new ArrayList<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SQL_SELECT_ALL_FACULTIES);
            processResult(faculties, rs);
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return faculties;
    }

    @Override
    public Faculty findEntityById(Integer id) throws ProjectException {
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
                faculty.setChecked(rs.getInt(CHECKED) == 1);
            }
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return faculty;
    }

    @Override
    public boolean delete(Integer id) throws ProjectException {
        int flag;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_DELETE_FACULTY)) {
            st.setInt(1 , id);
            flag = st.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Deletion error", e);
            throw new ProjectException("Deletion error", e);
        }
        return flag != 0;
    }

    @Override
    public boolean create(Faculty faculty) throws ProjectException {
        int flag;
        try {
            try (PreparedStatement st = connection.prepareStatement(SQL_INSERT_FACULTY)) {
                st.setString(1, faculty.getNameRu());
                st.setString(2, faculty.getNameEn());
                st.setInt(3, faculty.getSeatsPaid());
                st.setInt(4, faculty.getSeatsBudget());
                st.setInt(5, faculty.getId());
                flag = st.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.error("Insertion error", e);
            throw new ProjectException("Insertion error", e);
        }
        return flag != 0;
    }

    @Override
    public boolean update(Faculty faculty) throws ProjectException {
        int flag;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_UPDATE_FACULTY)) {
            st.setString(1, faculty.getNameRu());
            st.setString(2, faculty.getNameEn());
            st.setInt(3, faculty.getSeatsPaid());
            st.setInt(4, faculty.getSeatsBudget());
            int checked = faculty.isChecked() ? 1 : 0;
            st.setInt(5, checked);
            st.setInt(6, faculty.getId());
            flag = st.executeUpdate();
        } catch (SQLException e) {
            throw new ProjectException("Update error", e);
        }
        return flag != 0;
    }
    @Override
    public boolean checkStatus(Integer facultyId) throws ProjectException {
        boolean result = false;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_CHECK_FACULTY_STATUS)) {
            st.setInt(1, facultyId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                result = (rs.getInt(AVAILABLE) == 1);
            }
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return result;
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
            int checked = rs.getInt(CHECKED);
            faculty.setChecked(checked == 1);
            faculties.add(faculty);
        }
    }

    static {
        SQL_SELECT_ALL_FACULTIES =
                "SELECT  `id`, " +
                        "`name_ru`, " +
                        "`name_en`, " +
                        "`seats_paid`, " +
                        "`seats_budget`, " +
                        "`checked` " +
                "FROM `faculties` " +
                "WHERE `available` = 1";
        SQL_SELECT_FACULTY_BY_ID =
                "SELECT  `id`, " +
                        "`name_ru`, " +
                        "`name_en`, " +
                        "`seats_paid`, " +
                        "`seats_budget`, " +
                        "`checked` " +
                "FROM `faculties` " +
                "WHERE `id` = ?";
        SQL_INSERT_FACULTY =
                "INSERT INTO `faculties` " +
                "(`name_ru`,`name_en`,`seats_paid`,`seats_budget`,`id`) " +
                "VALUES (?,?,?,?,?)";

        SQL_DELETE_FACULTY =
                "UPDATE `faculties` " +
                "SET    `available` = 0 " +
                "WHERE  `id` = ?";
        // checked added
        SQL_UPDATE_FACULTY =
                "UPDATE  `faculties` " +
                "SET     `name_ru` = ?, " +
                        "`name_en` = ?, " +
                        "`seats_paid` = ?, " +
                        "`seats_budget` = ?, " +
                        "`checked` = ? " +
                "WHERE `id` = ?";
        SQL_CHECK_FACULTY_STATUS =
                "SELECT `available` " +
                "FROM `faculties` " +
                "WHERE `id` = ?";
    }
}