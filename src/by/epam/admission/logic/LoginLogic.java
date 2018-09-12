package by.epam.admission.logic;

import by.epam.admission.dao.TransactionHelper;
import by.epam.admission.dao.impl.UserDao;
import by.epam.admission.model.User;

public class LoginLogic {

    public static User checkLogin(String login, String password) {

        TransactionHelper helper = new TransactionHelper();
        UserDao userDao = new UserDao();

        helper.startTransaction(userDao);
        User user = userDao.findUserByEmailAndPassword(login, password);
        helper.endTransaction();

        return user;
    }

}
