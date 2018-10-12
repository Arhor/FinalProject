/*
 * class: RegisterCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.service.MailService;
import by.epam.admission.service.UserService;
import by.epam.admission.model.User;
import by.epam.admission.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Burishinets Maxim
 * @version 1.0 05 Sep 2018
 */
public class RegisterCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(RegisterCommand.class);

    @Override
    public Router execute(HttpServletRequest request) {

        String page;
        Router router = new Router();
        HttpSession session = request.getSession();

        String email = request.getParameter(Names.EMAIL);
        String password = request.getParameter(Names.PASSWORD);
        String firstName = request.getParameter(Names.FIRST_NAME);
        String lastName = request.getParameter(Names.LAST_NAME);
        String language = request.getParameter(Names.LANGUAGE);

        email = XssFilter.doFilter(email);
        firstName = XssFilter.doFilter(firstName);
        lastName = XssFilter.doFilter(lastName);

        String locale = (String) session.getAttribute("locale");
        User.Lang lang = User.Lang.getLang(locale);

        try {
            boolean validEmail = InputValidator.validate(
                    email, InputValidator.InputType.EMAIL);
            boolean validPassword = InputValidator.validate(
                    password, InputValidator.InputType.PASSWORD);
            boolean validFirstName = InputValidator.validate(
                    firstName, InputValidator.InputType.FIRST_NAME);
            boolean validLastName = InputValidator.validate(
                    lastName, InputValidator.InputType.LAST_NAME);

            if (validEmail && validPassword && validFirstName && validLastName) {
                if (UserService.checkEmail(email)) {
                    User user = new User();
                    user.setEmail(email);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setPassword(password);
                    user.setRole(User.Role.CLIENT);
                    switch (User.Lang.valueOf(language.toUpperCase())) {
                        case RU:
                            user.setLang(User.Lang.RU);
                            break;
                        case EN:
                            user.setLang(User.Lang.EN);
                            break;
                    }
                    session.setAttribute(Names.USER, user);
                    session.setAttribute(Names.PASSWORD, password);
                    String confirmCode = ConfirmationCodeGenerator.generate();
                    session.setAttribute(Names.CONFIRMATION_CODE,confirmCode);
                    ResourceBundle mailResources =
                            ResourceBundle.getBundle("resources.mail");
                    new MailService(
                            email,
                            Names.EMAIL_SUBJECT,
                            confirmCode,
                            mailResources).start();
                    page = ConfigurationManager.getProperty("path.page.confirm");
                    router.setType(Router.Type.REDIRECT);
                } else {
                    request.setAttribute(Names.REGISTRATION_ERROR, email + " "
                            + MessageManager.getProperty("message.registration.email.taken", lang));
                    page = ConfigurationManager.getProperty("path.page.registration");
                    router.setType(Router.Type.FORWARD);
                }
            } else {
                request.setAttribute(Names.REGISTRATION_ERROR, email + " "
                        + MessageManager.getProperty("message.registration.invalid", lang));
                page = ConfigurationManager.getProperty("path.page.registration");
                router.setType(Router.Type.FORWARD);
            }
            router.setPage(page);

        } catch (ProjectException e) {
            LOG.error("Registration error", e);
            router.setType(Router.Type.ERROR);
            router.setErrorCode(500);
        }
        return router;
    }
}
