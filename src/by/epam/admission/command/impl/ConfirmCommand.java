/*
 * class: ConfirmCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.RegisterLogic;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;
import by.epam.admission.util.MessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Burishinets Maxim
 * @version 1.0 10 Sep 2018
 */
public class ConfirmCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(ConfirmCommand.class);

    private static final String ATTR_USER = "user";
    private static final String ATTR_PASSWORD = "password";
    private static final String ATTR_CONFIRMATION_CODE = "confirmationCode";
    private static final String ATTR_ROLE = "role";
    private static final String ATTR_LOCALE = "locale";
    private static final String ATTR_ERROR_LOGIN_MESSAGE = "errorLoginMessage";

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) {
        Router router = new Router();
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute(ATTR_USER);
        String password = (String) session.getAttribute(ATTR_PASSWORD);

        String submittedCode = request.getParameter(ATTR_CONFIRMATION_CODE);
        String realCode = String.valueOf(session.getAttribute(
                ATTR_CONFIRMATION_CODE));

        try {
            String page;
            if (submittedCode.equals(realCode)) {
                user = RegisterLogic.registerUser(user, password);
                if (user != null) {
                    session.setAttribute(ATTR_USER, user);
                    session.setAttribute(ATTR_ROLE, user.getRole());
                    session.setAttribute(ATTR_LOCALE, user.getLang().getValue());
                    page = ConfigurationManager.getProperty(
                            "path.page.client.main");
                    router.setPage(page);
                    router.setType(Router.Type.FORWARD);
                } else {
                    String errorMessage =  MessageManager.getProperty(
                            "message.loginerror");
                    request.setAttribute(ATTR_ERROR_LOGIN_MESSAGE, errorMessage);
                    page = ConfigurationManager.getProperty("path.page.login");
                    router.setPage(page);
                    router.setType(Router.Type.FORWARD);
                }
            } else {
                session.removeAttribute(ATTR_USER);
                page = ConfigurationManager.getProperty("path.page.main");
                router.setPage(page);
                router.setType(Router.Type.FORWARD);
            }
        } catch (ProjectException e) {
            LOG.error(e);
            router.setType(Router.Type.ERROR);
            try {
                response.sendError(500);
            } catch (IOException e1) {
                LOG.error(e1);
            }
        } finally {
            session.removeAttribute(ATTR_CONFIRMATION_CODE);
            session.removeAttribute(ATTR_PASSWORD);
        }
        return router;
    }
}
