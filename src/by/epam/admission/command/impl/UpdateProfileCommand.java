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
import by.epam.admission.util.Names;
import by.epam.admission.util.XssFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
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

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        String page;
        HttpSession session = request.getSession();

        String firstName = request.getParameter(Names.FIRST_NAME);
        String lastName = request.getParameter(Names.LAST_NAME);
        String password = request.getParameter(Names.PASSWORD);

        firstName = XssFilter.doFilter(firstName);
        lastName = XssFilter.doFilter(lastName);

        User user = (User) session.getAttribute(Names.USER);

        try {
            if (user.getRole() == User.Role.CLIENT) {
                String city = request.getParameter(Names.CITY);
                String country = request.getParameter(Names.COUNTRY);
                String certificate = request.getParameter(Names.CERTIFICATE);

                city = XssFilter.doFilter(city);
                country = XssFilter.doFilter(country);
                certificate = XssFilter.doFilter(certificate);

                Enrollee enrollee = (Enrollee) session.getAttribute(Names.ENROLLEE);
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
                        session.setAttribute(Names.AVAILABLE_SUBJECTS, subjects);
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
                session.setAttribute(Names.ENROLLEE, enrollee);
            }

            user.setFirstName(firstName);
            user.setLastName(lastName);

            if (UserService.updateUser(user, password)) {
                session.setAttribute(Names.USER, user);
            }

            User.Role role = (User.Role) session.getAttribute(Names.ROLE);
            page = ProfileService.definePage(role);

            router.setPage(page);
            router.setType(Router.Type.FORWARD);
        } catch (ProjectException e) {
            LOG.error("Profile updating error", e);
            router.setType(Router.Type.ERROR);
            router.setErrorCode(500);
        }
        return router;
    }
}
