/*
 * class: RegisterCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.MailService;
import by.epam.admission.logic.UserService;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;
import by.epam.admission.util.ConfirmationCodeGenerator;
import by.epam.admission.util.EmailValidator;
import by.epam.admission.util.MessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Burishinets Maxim
 * @version 1.0 05 Sep 2018
 */
public class RegisterCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(RegisterCommand.class);

    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_FIRST_NAME = "firstName";
    private static final String PARAM_LAST_NAME = "lastName";
    private static final String PARAM_LANGUAGE = "language";
    private static final String EMAIL_SUBJECT = "Registration confirmation";
    private static final String ATTR_USER = "user";
    private static final String ATTR_PASSWORD = "password";
    private static final String ATTR_CONFIRMATION_CODE = "confirmationCode";
    private static final String ATTR_REGISTRATION_ERROR = "registrationError";

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) {

        String page;
        Router router = new Router();
        HttpSession session = request.getSession();

        String email = request.getParameter(PARAM_EMAIL);
        String password = request.getParameter(PARAM_PASSWORD);
        String firstName = request.getParameter(PARAM_FIRST_NAME);
        String lastName = request.getParameter(PARAM_LAST_NAME);
        String language = request.getParameter(PARAM_LANGUAGE);

        email = email.replaceAll("</?script>", "");
        firstName = firstName.replaceAll("</?script>", "");
        lastName = lastName.replaceAll("</?script>", "");

        try {
            if (EmailValidator.validate(email)) {
                if (UserService.checkEmail(email)) {
                    User user = new User();
                    user.setEmail(email);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setRole(User.Role.CLIENT);
                    switch (User.Lang.valueOf(language.toUpperCase())) {
                        case RU:
                            user.setLang(User.Lang.RU);
                            break;
                        case EN:
                            user.setLang(User.Lang.EN);
                            break;
                    }
                    session.setAttribute(ATTR_USER, user);
                    session.setAttribute(ATTR_PASSWORD, password);
                    String confirmCode = ConfirmationCodeGenerator.generate();
                    session.setAttribute(ATTR_CONFIRMATION_CODE,confirmCode);
                    ResourceBundle mailResources =
                            ResourceBundle.getBundle("resources.mail");
                    new MailService(
                            email,
                            EMAIL_SUBJECT,
                            confirmCode,
                            mailResources).start();
                    page = ConfigurationManager.getProperty("path.page.confirm");
                } else {
                    request.setAttribute(ATTR_REGISTRATION_ERROR, email + " "
                            + MessageManager.getProperty("message.email.inuse"));
                    page = ConfigurationManager.getProperty("path.page.registration");
                }
            } else {
                request.setAttribute(ATTR_REGISTRATION_ERROR, email + " "
                        + MessageManager.getProperty("message.email.invalid"));
                page = ConfigurationManager.getProperty("path.page.registration");
            }
            router.setPage(page);
            router.setType(Router.Type.FORWARD);
        } catch (ProjectException e) {
            LOG.error("Registration error", e);
            router.setType(Router.Type.ERROR);
            try {
                response.sendError(500);
            } catch (IOException e1) {
                LOG.error(e1);
            }
        }
        return router;
    }
}
