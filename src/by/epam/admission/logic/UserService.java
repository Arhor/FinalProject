package by.epam.admission.logic;

import by.epam.admission.dao.DaoHelper;
import by.epam.admission.dao.impl.UserDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.User;

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
}
