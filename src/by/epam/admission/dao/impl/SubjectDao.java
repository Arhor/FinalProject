/*
 * class: SubjectDao
 */

package by.epam.admission.dao.impl;

import by.epam.admission.dao.AbstractDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Subject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

/**
 * @author Maxim Burishinets
 * @version 1.0 2 Sep 2018
 */
public class SubjectDao extends AbstractDao<Integer, Subject> {

    private static final Logger LOG = LogManager.getLogger(SubjectDao.class);

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

//    @Override
    public List<Subject> findAll() throws ProjectException {
        ArrayList<Subject> subjects = new ArrayList<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SQL_SELECT_ALL_SUBJECTS);
            processResult(subjects, rs);
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return subjects;
    }

    @Override
    public Subject findEntityById(Integer id) throws ProjectException {
        Subject subject = null;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_SELECT_SUBJECT_BY_ID)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                subject = new Subject();
                subject.setId(id);
                subject.setNameRu(rs.getString(NAME_RU));
                subject.setNameEn(rs.getString(NAME_EN));
            }
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return subject;
    }

    public TreeSet<Subject> findSubjectsByFacultyId(int id) throws ProjectException {
        TreeSet<Subject> subjects = new TreeSet<>();
        try (PreparedStatement st = connection.prepareStatement(
                SQL_SELECT_SUBJECTS_BY_FACULTY_ID)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            processResult(subjects, rs);
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return subjects;
    }

    @Override
    public boolean delete(Integer id) throws ProjectException {
        int flag;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_DELETE_SUBJECT_BY_ID)) {
            st.setInt(1, id);
            flag = st.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Deletion error", e);
            throw new ProjectException("Deletion error", e);
        }
        return flag != 0;
    }

    @Override
    public boolean delete(Subject subject) throws ProjectException {
        try {
            return executeDMLQuery(subject, SQL_DELETE_SUBJECT);
        } catch (SQLException e) {
            LOG.error("Deletion error", e);
            throw new ProjectException("Deletion error", e);
        }
    }

    @Override
    public boolean create(Subject subject) throws ProjectException {
        try {
            return executeDMLQuery(subject, SQL_INSERT_SUBJECT);
        } catch (SQLException e) {
            LOG.error("Insertion error", e);
            throw new ProjectException("Insertion error", e);
        }
    }

//    @Override
    public Subject update(Subject subject) throws ProjectException {
        try {
            return executeDMLQuery(subject, SQL_UPDATE_SUBJECT) ? subject : null;
        } catch (SQLException e) {
            LOG.error("Update error", e);
            throw new ProjectException("Update error", e);
        }
    }

    private void processResult(Collection<Subject> subjects, ResultSet rs)
            throws SQLException {
        while (rs.next()) {
            Subject subject = new Subject();
            subject.setId(rs.getInt(ID));
            subject.setNameRu(rs.getString(NAME_RU));
            subject.setNameEn(rs.getString(NAME_EN));
            subjects.add(subject);
        }
    }

    private boolean executeDMLQuery(Subject entity, String query)
            throws SQLException {
        int flag;
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, entity.getNameRu());
            st.setString(2, entity.getNameEn());
            st.setInt(3, entity.getId());
            flag = st.executeUpdate();
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
