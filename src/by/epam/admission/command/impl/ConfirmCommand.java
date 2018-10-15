/*
 * class: ConfirmCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.service.UserService;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;
import by.epam.admission.util.MessageManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.epam.admission.command.Router.*;
import static by.epam.admission.util.Names.*;

/**
 * Class ConfirmCommand used for final stage of new user registration
 *
 * @author Burishinets Maxim
 * @version 1.0 10 Sep 2018
 * @see ActionCommand
 */
public class ConfirmCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(ConfirmCommand.class);

    /**
     * Method receives session attribute 'confirmationCode' that was sent to
     * user's e-mail and compares it with the entered code, if they are equal
     * then registration of new user initializes. On success user forwards to
     * the corresponding it's role home page, otherwise user is redirected to
     * error page
     *
     * @param request {@link HttpServletRequest} object received from
     *                controller-servlet
     * @return {@link Router} object that contains result of executing concrete
     * command
     */
    @Override
    public Router execute(HttpServletRequest request) {
        String page;
        Router router = new Router();
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute(USER);

        String submittedCode = request.getParameter(CONFIRMATION_CODE);
        String realCode = String.valueOf(session.getAttribute(
                CONFIRMATION_CODE));
        String locale = (String) session.getAttribute(LOCALE);
        User.Lang lang = User.Lang.getLang(locale);

        try {
            if (submittedCode.equals(realCode)) {
                user = UserService.registerUser(user);
                if (user != null) {
                    user.setPassword(null);
                    session.setAttribute(USER, user);
                    session.setAttribute(ROLE, user.getRole());
                    session.setAttribute(LOCALE, user.getLang().getValue());
                    page = ConfigurationManager.getProperty(
                            "path.page.client.main");
                    router.setPage(page);
                    router.setType(Type.REDIRECT);
                } else {
                    String errorMessage =  MessageManager.getProperty(
                            "message.registration.failed", lang);
                    request.setAttribute(ERROR_LOGIN_MESSAGE, errorMessage);
                    page = ConfigurationManager.getProperty("path.page.login");
                    router.setPage(page);
                    router.setType(Type.FORWARD);
                }
            } else {
                LOG.debug("Registration failed. Wrong confirmation code:\n" +
                        "Expected: " + realCode + "\n" +
                        "  Actual: " + submittedCode);
                session.removeAttribute(USER);
                page = ConfigurationManager.getProperty("path.page.main");
                router.setPage(page);
                router.setType(Type.FORWARD);
            }
        } catch (ProjectException e) {
            LOG.error("Registration confirm error", e);
            router.setType(Type.ERROR);
            router.setErrorCode(500);
        } finally {
            session.removeAttribute(CONFIRMATION_CODE);
            session.removeAttribute(PASSWORD);
        }
        return router;
    }
}
