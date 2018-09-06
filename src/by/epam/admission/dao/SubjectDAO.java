/*
 * class: SubjectDAO
 */

package by.epam.admission.dao;

import by.epam.admission.model.Subject;
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
 * @version 1.0 2 Sep 2018
 */
public class SubjectDAO extends AbstractDAO<Integer, Subject> {

    private static final Logger LOG = LogManager.getLogger(SubjectDAO.class);

    // SQL queries
    private static final String SQL_SELECT_ALL_SUBJECTS;
    private static final String SQL_SELECT_SUBJECT_BY_ID;
    private static final String SQL_INSERT_SUBJECT;
    private static final String SQL_DELETE_SUBJECT;
    private static final String SQL_DELETE_SUBJECT_BY_ID;
    private static final String SQL_UPDATE_SUBJECT;
    private static final String SQL_SELECT_SUBJECTS_BY_FACULTY_ID;

    // column labels
    private static final String ID = "id";
    private static final String NAME_RU = "name_ru";
    private static final String NAME_EN = "name_en";

    @Override
    public List<Subject> findAll() {
        ArrayList<Subject> subjects = new ArrayList<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SQL_SELECT_ALL_SUBJECTS);
            processResult(subjects, rs);
        } catch (SQLException e) {
            LOG.error("SQL exception", e);
        }
        return subjects;
    }

    @Override
    public Subject findEntityById(Integer id) {
        Subject subject = new Subject();
        try (PreparedStatement st = connection.prepareStatement(
                SQL_SELECT_SUBJECT_BY_ID)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                subject.setId(id);
                subject.setNameRu(rs.getString(NAME_RU));
                subject.setNameEn(rs.getString(NAME_EN));
            }
        } catch (SQLException e) {
            LOG.error("SQL exception", e);
        }
        return subject;
    }

    public List<Subject> findSubjectsByFacultyId(int id) {
        ArrayList<Subject> subjects = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(
                SQL_SELECT_SUBJECTS_BY_FACULTY_ID)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            processResult(subjects, rs);
        } catch (SQLException e) {
            LOG.error("SQL exception", e);
        }
        return subjects;
    }

    @Override
    public boolean delete(Integer id) {
        int flag = 0;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_DELETE_SUBJECT_BY_ID)) {
            flag = st.executeUpdate();
        } catch (SQLException e) {
            LOG.error("SQL exception", e);
        }
        return flag != 0;
    }

    @Override
    public boolean delete(Subject subject) {
        return executeDMLQuery(subject, SQL_DELETE_SUBJECT);
    }

    @Override
    public boolean create(Subject subject) {
        return executeDMLQuery(subject, SQL_INSERT_SUBJECT);
    }

    @Override
    public Subject update(Subject subject) {
        return executeDMLQuery(subject, SQL_UPDATE_SUBJECT) ? subject : null;
    }

    private void processResult(ArrayList<Subject> subjects, ResultSet rs)
            throws SQLException {
        while (rs.next()) {
            Subject subject = new Subject();
            subject.setId(rs.getInt(ID));
            subject.setNameRu(rs.getString(NAME_RU));
            subject.setNameEn(rs.getString(NAME_EN));
            subjects.add(subject);
        }
    }

    private boolean executeDMLQuery(Subject entity, String query) {
        int flag = 0;
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, entity.getNameRu());
            st.setString(2, entity.getNameEn());
            st.setInt(3, entity.getId());
            flag = st.executeUpdate();
        } catch (SQLException e) {
            LOG.error("SQL exception", e);
        }
        return flag != 0;
    }

    static {
        SQL_SELECT_ALL_SUBJECTS =
                "SELECT `id`, `name_ru`, `name_en` " +
                "FROM `subjects`";
        SQL_SELECT_SUBJECT_BY_ID =
                "SELECT `id`, `name_ru`, `name_en` " +
                "FROM `subjects` " +
                "WHERE `id` = ?";
        SQL_INSERT_SUBJECT =
                "INSERT INTO `subjects` (`name_ru`,`name_en`,`id`) " +
                "VALUES (?,?,?)";
        SQL_DELETE_SUBJECT =
                "DELETE FROM `subjects` " +
                "WHERE  `name_ru` = ? " +
                        "AND `name_en` = ? " +
                        "AND `id` = ?";
        SQL_DELETE_SUBJECT_BY_ID =
                "DELETE FROM `subjects` " +
                "WHERE `id` = ?";
        SQL_UPDATE_SUBJECT =
                "UPDATE `subjects` " +
                "SET `name_ru` = ?, `name_en` = ? " +
                "WHERE `id` = ?";
        SQL_SELECT_SUBJECTS_BY_FACULTY_ID =
                "SELECT  `subjects`.`id`, " +
                        "`subjects`.`name_ru`, " +
                        "`subjects`.`name_en` " +
                "FROM `subjects` " +
                "JOIN `faculties_has_subjects` " +
                "ON `subjects`.`id` = `faculties_has_subjects`.`subjects_id` " +
                "WHERE `faculties_has_subjects`.`faculties_id` = ?";
    }
}