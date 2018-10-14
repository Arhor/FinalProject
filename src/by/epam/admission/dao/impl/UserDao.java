/*
 * class: UserDao
 */

package by.epam.admission.dao.impl;

import by.epam.admission.dao.AbstractDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.util.EncryptAction;
import by.epam.admission.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Burishinets Maxim
 * @version 1.0 29 Aug 2018
 */
public class UserDao extends AbstractDao<Integer, User> {

    // SQL queries
    private static final String SQL_SELECT_USERS;
    private static final String SQL_INSERT_USER;
    private static final String SQL_UPDATE_USER;
    private static final String SQL_DELETE_USER_BY_ID;
    private static final String SQL_SELECT_USER_NAME_BY_EMAIL;
    private static final String SQL_SELECT_USER_BY_EMAIL;
    private static final String SQL_SELECT_USERS_PAGINATION;
    private static final String SQL_SELECT_USERS_TOTAL_AMOUNT;
    private static final String SQL_SELECT_USER_STATUS;
    private static final String SQL_RESTORE_USER_BY_ID;
    private static final String SQL_CHECK_PASSWORD;
    private static final String SQL_SELECT_USER_ID;

    // column labels
    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String LANG = "lang";
    private static final String ROLE = "role";

//    @Override
    public List<User> findAll(int pageNumber, int rowsPerPager)
            throws ProjectException {
        ArrayList<User> users = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(
                SQL_SELECT_USERS_PAGINATION)) {
            int leftBorder = pageNumber * rowsPerPager + 1;
            int rightBorder = rowsPerPager * (pageNumber + 1);
            st.setInt(1,leftBorder);
            st.setInt(2, rightBorder);
            ResultSet rs = st.executeQuery();
            processResult(users, rs);
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return users;
    }

    public int findTotalAmount() throws ProjectException {
        int totalAmount = 0;
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SQL_SELECT_USERS_TOTAL_AMOUNT);
            if (rs.next()) {
                totalAmount += rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return totalAmount;
    }

    @Override
    public User findEntityById(Integer id) throws ProjectException {
        User user = null;
        try (PreparedStatement st = connection.prepareStatement(
                String.format(SQL_SELECT_USERS, ID))) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                user = setUser(rs);
            }
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return user;
    }

    public int findUserId(String email) throws ProjectException {
        int id = -1;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_SELECT_USER_ID)) {
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return id;
    }

    public boolean checkEmail(String email) throws ProjectException {
        boolean result = false;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_SELECT_USER_BY_EMAIL)) {
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                result = (count == 0);
            }

        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return result;
    }

    public boolean checkUser(int userId) throws ProjectException {
        boolean result = false;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_SELECT_USER_STATUS)) {
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                result = (rs.getInt(1) == 1);
            }
        } catch (SQLException e) {
            throw new ProjectException("Selection error", e);
        }
        return result;
    }

    @Override
    public boolean delete(Integer id) throws ProjectException {
        int flag;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_DELETE_USER_BY_ID)) {
            st.setInt(1, id);
            flag = st.executeUpdate();
        } catch (SQLException e) {
            throw new ProjectException("Deletion error", e);
        }
        return flag != 0;
    }

    public boolean restore(int id) throws ProjectException {
        int flag;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_RESTORE_USER_BY_ID)) {
            st.setInt(1, id);
            flag = st.executeUpdate();
        } catch (SQLException e) {
            throw new ProjectException("Deletion error", e);
        }
        return flag != 0;
    }

    public boolean checkPassword(String email, String password)
            throws ProjectException {
        boolean result = false;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_CHECK_PASSWORD)) {
            String name = findNameByEmail(email);
            String encryptedPassword = EncryptAction.encrypt(password, name);
            st.setString(1, email);
            st.setString(2, encryptedPassword);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                result = (rs.getInt(1) == 1);
            }
        } catch (SQLException e) {
            throw new ProjectException("Check password error", e);
        }
        return result;
    }

    // if return false - user with the same email already exists
    @Override
    public boolean create(User user) throws ProjectException {
        boolean result = false;
        try {
            if (findNameByEmail(user.getEmail()) == null) {
                int flag;
                String password = user.getPassword();
                String encryptedPassword = EncryptAction.encrypt(
                        password, user.getFirstName());
                try (PreparedStatement st = connection.prepareStatement(
                        SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
                    st.setString(1, user.getEmail());
                    st.setString(2, encryptedPassword);
                    st.setString(3, user.getFirstName());
                    st.setString(4, user.getLastName());
                    st.setString(5, user.getRole().toString().toLowerCase());
                    st.setString(6, user.getLang().toString().toLowerCase());
                    flag = st.executeUpdate();
                    ResultSet rs = st.getGeneratedKeys();
                    if (rs.next()) {
                        user.setId(rs.getInt(1));
                    }
                }
                result = flag != 0;
            }
        } catch (SQLException e) {
            throw new ProjectException("Insertion error", e);
        }
        return result;
    }

    // password changes every time on first_name change
    @Override
    public boolean update(User user) throws ProjectException {
        int flag;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_UPDATE_USER)) {
            String name = findNameByEmail(user.getEmail());
            String password = user.getPassword();
            String encryptedPassword = EncryptAction.encrypt(password, name);
            String newPassword = EncryptAction.encrypt(password, user.getFirstName());
            st.setString(1, newPassword);
            st.setString(2, user.getFirstName());
            st.setString(3, user.getLastName());
            st.setString(4, user.getLang().toString().toLowerCase());
            st.setString(5, user.getEmail());
            st.setString(6, encryptedPassword);
            flag = st.executeUpdate();
        } catch (SQLException e) {
            throw new ProjectException("Updating error", e);
        }
        return flag != 0;
    }

    private void processResult(ArrayList<User> users, ResultSet rs)
            throws SQLException {
        while (rs.next()) {
            users.add(setUser(rs));
        }
    }

    private User setUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt(ID));
        user.setEmail(rs.getString(EMAIL));
        user.setFirstName(rs.getString(FIRST_NAME));
        user.setLastName(rs.getString(LAST_NAME));
        user.setLang(User.Lang.valueOf(rs.getString(LANG).toUpperCase()));
        user.setRole(User.Role.valueOf(rs.getString(ROLE).toUpperCase()));
        return user;
    }

    private String findNameByEmail(String email) throws SQLException {
        String name = null;
        try (PreparedStatement st = connection.prepareStatement(
                SQL_SELECT_USER_NAME_BY_EMAIL)) {
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                name = rs.getString(FIRST_NAME);
            }
        }
        return name;
    }

    static {
        SQL_SELECT_USERS_PAGINATION =
                "SELECT  `id`, " +
                        "`email`, " +
                        "`first_name`, " +
                        "`last_name`, " +
                        "`lang`, " +
                        "`role` " +
                "FROM   `users` " +
                "WHERE  `id` >= ? " +
                        "AND `id` <= ?";
        SQL_SELECT_USERS =
                "SELECT  `id`, " +
                        "`email`, " +
                        "`first_name`, " +
                        "`last_name`, " +
                        "`lang`, " +
                        "`role` " +
                "FROM `users` " +
                "WHERE %s = ?";

        SQL_SELECT_USER_BY_EMAIL =
                "SELECT COUNT(*) FROM `users` WHERE `email` = ?";
        SQL_INSERT_USER =
                "INSERT INTO `users` " +
                "(`email`,`password`,`first_name`,`last_name`,`role`,`lang`) " +
                "VALUES (?,?,?,?,?,?)";
        SQL_UPDATE_USER =
                "UPDATE  `users` " +
                "SET     `password` = ?," +
                        "`first_name` = ?, " +
                        "`last_name` = ?, " +
                        "`lang` = ? " +
                "WHERE  `email` = ? " +
                "AND `password` = ?";
        SQL_SELECT_USER_NAME_BY_EMAIL =
                "SELECT `first_name` " +
                "FROM `users` " +
                "WHERE `email` = ?";
        SQL_DELETE_USER_BY_ID =
                "UPDATE `users` " +
                "LEFT JOIN   `enrollees` " +
                "ON `users`.`id` = `enrollees`.`users_id` " +
                "LEFT JOIN   `enrollees_has_subjects` " +
                "ON `enrollees`.`id` = `enrollees_has_subjects`.`enrollees_id` " +
                "LEFT JOIN   `admission_list` " +
                "ON `enrollees`.`id` = `admission_list`.`enrollees_id`" +
                "SET     `users`.`available` = 0, " +
                        "`enrollees`.`available` = 0, " +
                        "`enrollees_has_subjects`.`available` = 0, " +
                        "`admission_list`.`available` = 0 " +
                "WHERE  `users`.`id` = ?";
        SQL_RESTORE_USER_BY_ID =
                "UPDATE `users` " +
                "LEFT JOIN   `enrollees` " +
                "ON `users`.`id` = `enrollees`.`users_id` " +
                "LEFT JOIN   `enrollees_has_subjects` " +
                "ON `enrollees`.`id` = `enrollees_has_subjects`.`enrollees_id` " +
                "LEFT JOIN   `admission_list` " +
                "ON `enrollees`.`id` = `admission_list`.`enrollees_id`" +
                "SET     `users`.`available` = 1, " +
                        "`enrollees`.`available` = 1, " +
                        "`enrollees_has_subjects`.`available` = 1, " +
                        "`admission_list`.`available` = 1 " +
                "WHERE   `users`.`id` = ?";
        SQL_SELECT_USERS_TOTAL_AMOUNT =
                "SELECT COUNT(*) " +
                "FROM `users`";
        SQL_SELECT_USER_STATUS =
                "SELECT `available` " +
                "FROM `users` " +
                "WHERE `id` = ?";
        SQL_CHECK_PASSWORD =
                "SELECT  COUNT(*) " +
                "FROM   `users` " +
                "WHERE  `email` = ? AND `password` = ? AND `available` = 1";
        SQL_SELECT_USER_ID =
                "SELECT `id` " +
                "FROM `users` " +
                "WHERE `email` = ?";
//        SQL_SELECT_USER_BY_EMAIL_AND_PASSWORD =
//                "SELECT  `id`, " +
//                        "`email`, " +
//                        "`first_name`, " +
//                        "`last_name`, " +
//                        "`lang`, " +
//                        "`role` " +
//                        "FROM `users`" +
//                        "WHERE  `email` = ? " +
//                        "AND `password` = ?" +
//                        "AND `available` = 1";
//        SQL_DELETE_USER_BY_EMAIL_AND_PASSWORD =
//                "UPDATE `users` " +
//                        "LEFT JOIN   `enrollees` " +
//                        "ON `users`.`id` = `enrollees`.`users_id` " +
//                        "LEFT JOIN   `enrollees_has_subjects` " +
//                        "ON `enrollees`.`id` = `enrollees_has_subjects`.`enrollees_id` " +
//                        "LEFT JOIN   `admission_list` " +
//                        "ON `enrollees`.`id` = `admission_list`.`enrollees_id`" +
//                        "SET    `users`.`available` = 0, " +
//                        "`enrollees`.`available` = 0, " +
//                        "`enrollees_has_subjects`.`available` = 0, " +
//                        "`admission_list`.`available` = 0 " +
//                        "WHERE  `users`.`email` = ?" +
//                        "AND `users`.`password` = ?" +
//                        "AND `users`.`available` = 1";
    }
}
