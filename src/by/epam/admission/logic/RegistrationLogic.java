package by.epam.admission.logic;

import by.epam.admission.command.impl.RegistrationCommand;
import by.epam.admission.dao.TransactionHelper;
import by.epam.admission.dao.impl.UserDao;
import by.epam.admission.exception.DaoException;
import by.epam.admission.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegistrationLogic {

    private static final Logger LOG = LogManager.getLogger(RegistrationCommand.class);

    public static User registerUser(User user, String password) {

        TransactionHelper helper = new TransactionHelper();
        UserDao userDao = new UserDao();

        boolean result = false;

        helper.startTransaction(userDao);
        try {
            result = userDao.create(user, password);
            helper.commit();
        } catch (DaoException e) {
            LOG.error("Registration error", e); // TODO: implement registration error handling by throwing ServiceException
            helper.rollback();
        }
        helper.endTransaction();

        return result ? user : null;
    }
}
