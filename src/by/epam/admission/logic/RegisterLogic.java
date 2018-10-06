/*
 * class: RegisterLogic
 */

package by.epam.admission.logic;

import by.epam.admission.dao.DaoHelper;
import by.epam.admission.dao.impl.UserDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.User;

/**
 * @author Burishinets Maxim
 * @version 1.0 10 Sep 2018
 */
public class RegisterLogic {

    public static User registerUser(User user, String password)
            throws ProjectException {

        boolean result = false;
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
