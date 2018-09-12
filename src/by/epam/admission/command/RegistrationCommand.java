package by.epam.admission.command;

import by.epam.admission.dao.TransactionHelper;
import by.epam.admission.dao.impl.UserDao;
import by.epam.admission.exception.DaoException;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;
import by.epam.admission.util.MessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RegistrationCommand implements ActionCommand {

    private static final Logger LOG = LogManager.getLogger(RegistrationCommand.class);

    @Override
    public String execute(HttpServletRequest request) {
        String page;

        HttpSession session = request.getSession();

        User user = new User();
        user.setEmail(request.getParameter("email"));
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));

        user.setRole(User.Role.CLIENT);
        user.setLang(User.Lang.RU);

        String password = request.getParameter("password");

        LOG.debug("User to create: " + user);

        TransactionHelper helper = new TransactionHelper();
        UserDao userDao = new UserDao();
        helper.startTransaction(userDao);
        boolean result = false;
        try {
            result = userDao.create(user, password);
            helper.commit();
        } catch (DaoException e) {
            LOG.error("Registration error", e); // TODO: implement registration error handling
            helper.rollback();
        }

        helper.endTransaction();

        if (result) {
            request.setAttribute("user", user.getFirstName() + " " + user.getLastName());
            session.setAttribute("role", user.getRole());
            page = ConfigurationManager.getProperty("path.page.main");
        } else {
            request.setAttribute("errorLoginMessage", MessageManager.getProperty("message.loginerror"));
            page = ConfigurationManager.getProperty("path.page.login");
        }
        return page;
    }
}
