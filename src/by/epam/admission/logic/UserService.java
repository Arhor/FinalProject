package by.epam.admission.logic;

import by.epam.admission.dao.TransactionHelper;
import by.epam.admission.dao.impl.UserDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.User;

public class UserService {

    public static boolean updateUser(User user, String password) {
        boolean result = false;
        TransactionHelper transactionHelper = new TransactionHelper();
        UserDao userDao = new UserDao();
        transactionHelper.startTransaction(userDao);
        try {
            result = userDao.update(user, password);
            transactionHelper.commit();
        } catch (ProjectException e) {
            transactionHelper.rollback();
        } finally {
            transactionHelper.endTransaction();
        }
        return result;
    }
}
