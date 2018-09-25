package by.epam.admission.logic;

import by.epam.admission.dao.DaoHelper;
import by.epam.admission.dao.impl.UserDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.User;

public class LoginLogic {

    public static User checkLogin(String login, String password) throws ProjectException {
        DaoHelper helper = new DaoHelper();
        UserDao userDao = new UserDao();
        User user;
        helper.startTransaction(userDao);
        try {
            user = userDao.findUserByEmailAndPassword(login, password);
        } finally {
            helper.endTransaction();
        }
        return user;
    }

}
