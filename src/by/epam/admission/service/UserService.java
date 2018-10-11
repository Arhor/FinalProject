/*
 * class: UserService
 */

package by.epam.admission.service;

import by.epam.admission.dao.DaoHelper;
import by.epam.admission.dao.impl.UserDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.User;

import java.util.HashMap;
import java.util.List;

/**
 * @author Burishinets Maxim
 * @version 1.0 10 Sep 2018
 */
public class UserService {

    private UserService() {}

    public static User findUser(String login, String password)
            throws ProjectException {
        DaoHelper helper = new DaoHelper();
        UserDao userDao = new UserDao();
        User user = null;
        helper.startTransaction(userDao);
        try {
            if (userDao.checkPassword(login, password)) {
                int userId = userDao.findUserId(login);
                user = userDao.findEntityById(userId);
            }
        } finally {
            helper.endTransaction();
        }
        return user;
    }

    public static List<User> findUsers(int currPage, int rowsPerPage)
            throws ProjectException {
        List<User> users;
        DaoHelper helper = new DaoHelper();
        UserDao userDao = new UserDao();
        try {
            helper.startTransaction(userDao);
            users = userDao.findAll(currPage, rowsPerPage);
        } finally {
            helper.endTransaction();
        }
        return users;
    }

    public static int findTotalUsersAmount() throws ProjectException {
        int usersAmount;
        DaoHelper helper = new DaoHelper();
        UserDao userDao = new UserDao();
        try {
            helper.startTransaction(userDao);
            usersAmount = userDao.findTotalAmount();
        } finally {
            helper.endTransaction();
        }
        return usersAmount;
    }

    public static boolean updateUser(User user, String password)
            throws ProjectException {
        boolean result;
        DaoHelper daoHelper = new DaoHelper();
        UserDao userDao = new UserDao();

        try {
            daoHelper.startTransaction(userDao);
            result = userDao.update(user, password);
            daoHelper.commit();
        } catch (ProjectException e) {
            daoHelper.rollback();
            throw e;
        } finally {
            daoHelper.endTransaction();
        }
        return result;
    }

    public static HashMap<Integer, Boolean> checkUsers(String[] userIds)
            throws ProjectException {
        HashMap<Integer, Boolean> resultSet = new HashMap<>();
        DaoHelper helper = new DaoHelper();
        UserDao userDao = new UserDao();
        try {
            helper.startTransaction(userDao);
            for (String userId : userIds) {
                int uid = Integer.parseInt(userId);
                boolean result = userDao.checkUser(uid);
                resultSet.put(uid, result);
            }
        } finally {
            helper.endTransaction();
        }
        return resultSet;
    }

    public static boolean blockUser(int userId) throws ProjectException {
        boolean result;
        DaoHelper daoHelper = new DaoHelper();
        UserDao userDao = new UserDao();
        daoHelper.startTransaction(userDao);
        try {
            result = userDao.delete(userId);
            daoHelper.commit();
        } catch (ProjectException e) {
            daoHelper.rollback();
            throw e;
        } finally {
            daoHelper.endTransaction();
        }
        return result;
    }

    public static boolean unblockUser(int userId) throws ProjectException {
        boolean result;
        DaoHelper daoHelper = new DaoHelper();
        UserDao userDao = new UserDao();
        daoHelper.startTransaction(userDao);
        try {
            result = userDao.restore(userId);
            daoHelper.commit();
        } catch (ProjectException e) {
            daoHelper.rollback();
            throw e;
        } finally {
            daoHelper.endTransaction();
        }
        return result;
    }

    public static boolean checkEmail(String email) throws ProjectException {
        boolean result;
        DaoHelper helper = new DaoHelper();
        UserDao userDao = new UserDao();
        try {
            helper.startTransaction(userDao);
            result = userDao.checkEmail(email);
        } finally {
            helper.endTransaction();
        }
        return result;
    }

    public static User registerUser(User user, String password)
            throws ProjectException {

        boolean result;
        DaoHelper helper = new DaoHelper();
        UserDao userDao = new UserDao();
        try {
            helper.startTransaction(userDao);
            result = userDao.create(user, password);
            helper.commit();
        } catch (ProjectException e) {
            helper.rollback();
            throw e;
        } finally {
            helper.endTransaction();
        }
        return result ? user : null;
    }
}
