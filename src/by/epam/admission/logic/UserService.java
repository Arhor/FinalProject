package by.epam.admission.logic;

import by.epam.admission.dao.DaoHelper;
import by.epam.admission.dao.impl.EnrolleeDao;
import by.epam.admission.dao.impl.UserDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.User;

import java.util.HashMap;

public class UserService {

    public static boolean updateUser(User user, String password) {
        boolean result = false;
        DaoHelper daoHelper = new DaoHelper();
        UserDao userDao = new UserDao();
        daoHelper.startTransaction(userDao);
        try {
            result = userDao.update(user, password);
            daoHelper.commit();
        } catch (ProjectException e) {
            daoHelper.rollback();
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
        } catch (ProjectException e) {
            throw e;
        } finally {
            helper.endTransaction();
        }
        return resultSet;
    }

    public static boolean blockUser(int userId) throws ProjectException {
        boolean result = false;
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
        boolean result = false;
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
}
