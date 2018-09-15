package by.epam.admission.logic;

import by.epam.admission.dao.TransactionHelper;
import by.epam.admission.dao.impl.UserDao;
import by.epam.admission.exception.ProjectException;

public class CheckEmailLogic {

    public static boolean checkEmail(String email) throws ProjectException {
        boolean result;
        TransactionHelper helper = new TransactionHelper();
        UserDao userDao = new UserDao();
        try {
            helper.startTransaction(userDao);
            result = userDao.checkEmail(email);
        } finally {
            helper.endTransaction();
        }
        return result;
    }
}
