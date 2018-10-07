/*
 * class: UpdateProfileCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.EnrolleeService;
import by.epam.admission.logic.ProfileService;
import by.epam.admission.logic.SubjectService;
import by.epam.admission.logic.UserService;
import by.epam.admission.model.Enrollee;
import by.epam.admission.model.Subject;
import by.epam.admission.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
    private static final String ATTR_ROLE = "role";
    private static final String ATTR_AVAILABLE_SUBJECTS = "availableSubjects";

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) {
        Router router = new Router();
        String page;
        HttpSession session = request.getSession();

        String firstName = request.getParameter(PARAM_FIRST_NAME);
        String lastName = request.getParameter(PARAM_LAST_NAME);
        String password = request.getParameter(PARAM_PASSWORD);

        firstName = firstName.replaceAll("</?script>", "");
        lastName = lastName.replaceAll("</?script>", "");

        User user = (User) session.getAttribute(ATTR_USER);

        try {
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

                    List<Subject> subjects = SubjectService.findSubjects();
                    if (subjects != null) {
                        subjects.removeAll(enrollee.getMarks().keySet());
                        session.setAttribute(ATTR_AVAILABLE_SUBJECTS, subjects);
                    }
                    boolean registrationResult =
                            EnrolleeService.registerEnrollee(enrollee);
                    if (!registrationResult) {
                        enrollee = null;
                    }
                } else {
                    enrollee.setCity(city);
                    enrollee.setCountry(country);
                    enrollee.setSchoolCertificate(Integer.parseInt(certificate));
                    enrollee = EnrolleeService.updateEnrollee(enrollee);
                }
                session.setAttribute(ATTR_ENROLLEE, enrollee);
            }

            user.setFirstName(firstName);
            user.setLastName(lastName);

            if (UserService.updateUser(user, password)) {
                session.setAttribute(ATTR_USER, user);
            }

            User.Role role = (User.Role) session.getAttribute(ATTR_ROLE);
            page = ProfileService.definePage(role);

            router.setPage(page);
            router.setType(Router.Type.FORWARD);
        } catch (ProjectException e) {
            LOG.error("Profile updating error", e);
            try {
                response.sendError(500);
            } catch (IOException e1) {
                LOG.error(e);
            }
        }
        return router;
    }
}
