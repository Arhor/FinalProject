package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.logic.RegistrationLogic;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;
import by.epam.admission.util.MessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RegistrationCommand implements ActionCommand {

    private static final Logger LOG = LogManager.getLogger(RegistrationCommand.class);

    private static final String PARAM_NAME_LOGIN = "login";
    private static final String PARAM_NAME_PASSWORD = "password";
    private static final String PARAM_NAME_FIRST_NAME = "firstName";
    private static final String PARAM_NAME_LAST_NAME = "lastName";

    @Override
    public String execute(HttpServletRequest request) {
        String page;

        HttpSession session = request.getSession();

        String login = request.getParameter(PARAM_NAME_LOGIN);
        String password = request.getParameter(PARAM_NAME_PASSWORD);
        String firstName = request.getParameter(PARAM_NAME_FIRST_NAME);
        String lastName = request.getParameter(PARAM_NAME_LAST_NAME);

        User user = new User();

        user.setEmail(login);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(User.Role.CLIENT);
        user.setLang(User.Lang.RU);


        LOG.debug("User to create: " + user);

        user = RegistrationLogic.registerUser(user, password);

        LOG.debug("Registration result: " + user);

        if (user != null) {
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
