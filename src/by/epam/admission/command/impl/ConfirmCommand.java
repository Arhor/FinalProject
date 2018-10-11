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
import by.epam.admission.util.Names;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Burishinets Maxim
 * @version 1.0 10 Sep 2018
 */
public class ConfirmCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(ConfirmCommand.class);

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute(Names.USER);
        String password = (String) session.getAttribute(Names.PASSWORD);

        String submittedCode = request.getParameter(Names.CONFIRMATION_CODE);
        String realCode = String.valueOf(session.getAttribute(
                Names.CONFIRMATION_CODE));

        try {
            String page;
            if (submittedCode.equals(realCode)) {
                user = UserService.registerUser(user, password);
                if (user != null) {
                    session.setAttribute(Names.USER, user);
                    session.setAttribute(Names.ROLE, user.getRole());
                    session.setAttribute(Names.LOCALE, user.getLang().getValue());
                    page = ConfigurationManager.getProperty(
                            "path.page.client.main");
                    router.setPage(page);
                    router.setType(Router.Type.FORWARD);
                } else {
                    String errorMessage =  MessageManager.getProperty(
                            "message.loginerror");
                    request.setAttribute(Names.ERROR_LOGIN_MESSAGE, errorMessage);
                    page = ConfigurationManager.getProperty("path.page.login");
                    router.setPage(page);
                    router.setType(Router.Type.FORWARD);
                }
            } else {
                session.removeAttribute(Names.USER);
                page = ConfigurationManager.getProperty("path.page.main");
                router.setPage(page);
                router.setType(Router.Type.FORWARD);
            }
        } catch (ProjectException e) {
            LOG.error("Registration confirm error", e);
            router.setType(Router.Type.ERROR);
            router.setErrorCode(500);
        } finally {
            session.removeAttribute(Names.CONFIRMATION_CODE);
            session.removeAttribute(Names.PASSWORD);
        }
        return router;
    }
}
