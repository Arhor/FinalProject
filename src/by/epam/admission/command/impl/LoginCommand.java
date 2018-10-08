/*
 * class: LoginCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.EnrolleeService;
import by.epam.admission.logic.SubjectService;
import by.epam.admission.logic.UserService;
import by.epam.admission.model.Enrollee;
import by.epam.admission.model.Subject;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;
import by.epam.admission.util.MessageManager;
import by.epam.admission.util.Names;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @author Burishinets Maxim
 * @version 1.0 05 Sep 2018
 */
public class LoginCommand implements ActionCommand {

    private static final Logger LOG = LogManager.getLogger(LoginCommand.class);

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        String page;
        Router router = new Router();
        HttpSession session = request.getSession();
        String email = request.getParameter(Names.EMAIL);
        String password = request.getParameter(Names.PASSWORD);
        User user;
        try {
            if (!UserService.checkEmail(email)) {
                user = UserService.findUser(email, password);
                if(user != null) {
                    session.setAttribute(Names.USER, user);
                    session.setAttribute(Names.ROLE, user.getRole());
                    session.setAttribute(Names.LOCALE, user.getLang().getValue());
                    if (user.getRole() == User.Role.CLIENT) {
                        int userId = user.getId();
                        Enrollee enrollee = EnrolleeService.findEnrollee(userId);
                        if (enrollee != null) {
                            List<Subject> subjects = SubjectService.findSubjects();
                            subjects.removeAll(enrollee.getMarks().keySet());
                            session.setAttribute(Names.AVAILABLE_SUBJECTS, subjects);
                        }
                        session.setAttribute(Names.ENROLLEE, enrollee);
                    }
                    switch (user.getRole()) {
                        case CLIENT:
                            page = ConfigurationManager.getProperty(
                                    "path.page.client.main");
                            break;
                        case ADMIN:
                            page = ConfigurationManager.getProperty(
                                    "path.page.admin.main");
                            break;
                        case GUEST:
                        default:
                            page = ConfigurationManager.getProperty(
                                    "path.page.main");
                    }
                    router.setType(Router.Type.REDIRECT);
                } else {
                    String message = MessageManager.getProperty("message.loginerror");
                    request.setAttribute(Names.ERROR_LOGIN_MESSAGE, message);
                    page = ConfigurationManager.getProperty("path.page.login");
                    router.setType(Router.Type.FORWARD);
                }
            } else {
                String message = MessageManager.getProperty("message.loginerror");
                request.setAttribute(Names.ERROR_LOGIN_MESSAGE, message);
                page = ConfigurationManager.getProperty("path.page.login");
                router.setType(Router.Type.FORWARD);
            }
            router.setPage(page);
        } catch (ProjectException e) {
            LOG.error(e);
            LOG.debug("ERROR MESSAGE: ", e);
            router.setType(Router.Type.ERROR);
            response.sendError(500);
        }
        return router;
    }

}
