/*
 * class: UpdateProfileCommand
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.TreeMap;

/**
 * @author Burishinets Maxim
 * @version 1.0 10 Sep 2018
 */
public class UpdateProfileCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(UpdateProfileCommand.class);

    private static final String PARAM_FIRST_NAME = "firstName";
    private static final String PARAM_LAST_NAME = "lastName";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_CITY = "city";
    private static final String PARAM_COUNTRY = "country";
    private static final String PARAM_CERTIFICATE = "certificate";
    private static final String ATTR_USER = "user";
    private static final String ATTR_ENROLLEE = "enrollee";

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) {
        Router router = new Router();

        HttpSession session = request.getSession();

        String firstName = request.getParameter(PARAM_FIRST_NAME);
        String lastName = request.getParameter(PARAM_LAST_NAME);
        String password = request.getParameter(PARAM_PASSWORD);

        firstName = firstName.replaceAll("</?script>", "");
        lastName = lastName.replaceAll("</?script>", "");

        User user = (User) session.getAttribute(ATTR_USER);

        if (user.getRole() == User.Role.CLIENT) {
            String city = request.getParameter(PARAM_CITY);
            String country = request.getParameter(PARAM_COUNTRY);
            String certificate = request.getParameter(PARAM_CERTIFICATE);

            city = city.replaceAll("</?script>", "");
            country = country.replaceAll("</?script>", "");
            certificate = certificate.replaceAll("</?script>", "");

            Enrollee enrollee = (Enrollee) session.getAttribute(ATTR_ENROLLEE);
            if (enrollee == null) {
                enrollee = new Enrollee();
                enrollee.setUserId(user.getId());
                enrollee.setCity(city);
                enrollee.setCountry(country);
                enrollee.setSchoolCertificate(Integer.parseInt(certificate));
                enrollee.setMarks(new TreeMap<>());

                List<Subject> subjects = null;
                try {
                    subjects = SubjectService.findSubjects();
                } catch (ProjectException e) {
                    LOG.error(e);
                }
                subjects.removeAll(enrollee.getMarks().keySet());
                session.setAttribute("availableSubjects", subjects);

                if (!EnrolleeService.registerEnrollee(enrollee)) {
                    enrollee = null;
                }
            } else {
                enrollee.setCity(city);
                enrollee.setCountry(country);
                enrollee.setSchoolCertificate(Integer.parseInt(certificate));
                enrollee = EnrolleeService.updateEnrollee(enrollee);
            }
            session.setAttribute("enrollee", enrollee);
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);

        if (UserService.updateUser(user, password)) {
            session.setAttribute("user", user);
        }

        String page;

        switch ((User.Role) session.getAttribute("role")) {
            case CLIENT:
                page = ConfigurationManager.getProperty("path.page.client.profile");
                break;
            case ADMIN:
                page = ConfigurationManager.getProperty("path.page.admin.profile");
                break;
            case GUEST:
            default:
                page = ConfigurationManager.getProperty("path.page.main");
        }

        router.setPage(page);
        router.setType(Router.Type.FORWARD);
        return router;
    }
}
