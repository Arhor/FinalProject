package by.epam.admission.logic;

import by.epam.admission.command.impl.RegisterCommand;
import by.epam.admission.dao.DaoHelper;
import by.epam.admission.dao.impl.UserDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegisterLogic {

    private static final Logger LOG = LogManager.getLogger(RegisterCommand.class);

    public static User registerUser(User user, String password) {
        DaoHelper helper = new DaoHelper();
        UserDao userDao = new UserDao();
        boolean result = false;
        helper.startTransaction(userDao);
        try {
            result = userDao.create(user, password);
            helper.commit();
        } catch (ProjectException e) {
            LOG.error("Registration error", e); // TODO: implement registration error handling
            helper.rollback();
        } finally {
            helper.endTransaction();
        }
        return result ? user : null;
    }
}
