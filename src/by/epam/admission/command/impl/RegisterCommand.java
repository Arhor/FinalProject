package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.CheckEmailLogic;
import by.epam.admission.logic.MailLogic;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;
import by.epam.admission.util.ConfirmationCodeGenerator;
import by.epam.admission.util.MessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ResourceBundle;

public class RegisterCommand implements ActionCommand {

    private static final Logger LOG = LogManager.getLogger(RegisterCommand.class);

    private static final String PARAM_NAME_EMAIL = "email";
    private static final String PARAM_NAME_PASSWORD = "password";
    private static final String PARAM_NAME_FIRST_NAME = "firstName";
    private static final String PARAM_NAME_LAST_NAME = "lastName";
    private static final String CONFIRMATION_SUBJECT = "Registration confirmation";

    @Override
    public String execute(HttpServletRequest request) {

        String page;

        HttpSession session = request.getSession();

        String email = request.getParameter(PARAM_NAME_EMAIL);
        String password = request.getParameter(PARAM_NAME_PASSWORD);
        String firstName = request.getParameter(PARAM_NAME_FIRST_NAME);
        String lastName = request.getParameter(PARAM_NAME_LAST_NAME);

        try {
            if (CheckEmailLogic.checkEmail(email)) {
                User user = new User();
                user.setEmail(email);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setRole(User.Role.CLIENT);
                user.setLang(User.Lang.RU);
                session.setAttribute("user", user);
                session.setAttribute("password", password);
                String confirmationCode = ConfirmationCodeGenerator.generate();
                session.setAttribute("confirmationCode",confirmationCode);
                ResourceBundle mailResources =
                        ResourceBundle.getBundle("resources.mail");
                new MailLogic(
                        email,
                        CONFIRMATION_SUBJECT,
                        confirmationCode,
                        mailResources).start();
                page = ConfigurationManager.getProperty("path.page.confirm");
            } else {
                request.setAttribute("registrationError", email + " "
                        + MessageManager.getProperty("message.registererror"));
                page = ConfigurationManager.getProperty("path.page.registration");
            }
        } catch (ProjectException e) {
            LOG.error("Registration error", e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
