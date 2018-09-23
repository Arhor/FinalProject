package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.EnrolleeService;
import by.epam.admission.logic.LoginLogic;
import by.epam.admission.model.Enrollee;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;
import by.epam.admission.util.MessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.routing.Route;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginCommand implements ActionCommand {

    private static final Logger LOG = LogManager.getLogger(LoginCommand.class);

    private static final String PARAM_NAME_EMAIL = "email";
    private static final String PARAM_NAME_PASSWORD = "password";

    @Override
    public Router execute(HttpServletRequest request) {
        String page;
        Router router = new Router();
        String login = request.getParameter(PARAM_NAME_EMAIL);
        String password = request.getParameter(PARAM_NAME_PASSWORD);

        HttpSession session = request.getSession();

        User user;
        try {
            user = LoginLogic.checkLogin(login, password);
            if(user != null) {
                session.setAttribute("user", user);
                session.setAttribute("role", user.getRole());
                session.setAttribute("locale", user.getLang().getValue());
                if (user.getRole() == User.Role.CLIENT) {
                    Enrollee enrollee = EnrolleeService.findEnrollee(user.getId());
                    LOG.debug(enrollee);
                    session.setAttribute("enrollee", enrollee);
                }
                switch (user.getRole()) {
                    case CLIENT:
                        page = ConfigurationManager.getProperty("path.page.client.main");
                        break;
                    case ADMIN:
                        page = ConfigurationManager.getProperty("path.page.admin.main");
                        break;
                    case GUEST:
                    default:
                        page = ConfigurationManager.getProperty("path.page.main");
                }

                router.setType(Router.Type.FORWARD);
            } else {
                String message = MessageManager.getProperty("message.loginerror");
                request.setAttribute("errorLoginMessage", message);
                page = ConfigurationManager.getProperty("path.page.login");
                router.setType(Router.Type.FORWARD);
            }
        } catch (ProjectException e) {
            LOG.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
            router.setType(Router.Type.FORWARD);
        }

        router.setPage(page);
        return router;
    }

}
