/*
 * class: UpdateProfileCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.service.*;
import by.epam.admission.model.Enrollee;
import by.epam.admission.model.Subject;
import by.epam.admission.model.User;
import by.epam.admission.util.MessageManager;
import by.epam.admission.util.XssFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.TreeMap;

import static by.epam.admission.util.InputValidator.*;
import static by.epam.admission.util.Names.*;

/**
 * Class UpdateProfileCommand used to update current user profile
 *
 * @author Burishinets Maxim
 * @version 1.0 10 Sep 2018
 * @see ActionCommand
 */
public class UpdateProfileCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(UpdateProfileCommand.class);

    /**
     * Method used to update user or user/enrollee profile. Depending on current
     * user role method receives user's inputs as request parameters. Filters if
     * for XSS tries after that validates. If user submitted valid text lines
     * and password - method invokes user or user/enrollee services to update
     * profile
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

        String firstName = request.getParameter(FIRST_NAME);
        String lastName = request.getParameter(LAST_NAME);
        String password = request.getParameter(PASSWORD);

        firstName = XssFilter.doFilter(firstName);
        lastName = XssFilter.doFilter(lastName);

        User currUser = (User) session.getAttribute(USER);
        currUser.setPassword(password);
        User.Role role = currUser.getRole();

        String locale = (String) session.getAttribute(LOCALE);
        User.Lang lang = User.Lang.getLang(locale);

        try {
            boolean validFirstName = validate(firstName, InputType.FIRST_NAME);
            boolean validLastName = validate(lastName, InputType.LAST_NAME);

            if (validFirstName && validLastName) {

                User user = UserService.findUser(currUser.getEmail(), password);

                if (user != null && user.equals(currUser)) {

                    currUser.setFirstName(firstName);
                    currUser.setLastName(lastName);

                    if (UserService.updateUser(currUser)) {
                        session.setAttribute(USER, currUser);
                    }

                    if (role == User.Role.CLIENT && FacultyService.checkAvailability()) {
                        String city = request.getParameter(CITY);
                        String country = request.getParameter(COUNTRY);
                        String certificate = request.getParameter(CERTIFICATE);

                        city = XssFilter.doFilter(city);
                        country = XssFilter.doFilter(country);
                        certificate = XssFilter.doFilter(certificate);

                        boolean validCity = validate(city, InputType.CITY);
                        boolean validCountry = validate(country, InputType.COUNTRY);
                        boolean validCertificate = validate(certificate, InputType.CERTIFICATE);

                        if (validCity && validCountry && validCertificate) {
                            Enrollee enrollee = (Enrollee) session.getAttribute(ENROLLEE);
                            if (enrollee == null) {
                                enrollee = new Enrollee();
                                enrollee.setUserId(currUser.getId());
                                enrollee.setCity(city);
                                enrollee.setCountry(country);
                                enrollee.setSchoolCertificate(Integer.parseInt(certificate));
                                enrollee.setMarks(new TreeMap<>());

                                List<Subject> subjects = SubjectService.findSubjects();
                                if (subjects != null) {
                                    subjects.removeAll(enrollee.getMarks().keySet());
                                    session.setAttribute(AVAILABLE_SUBJECTS, subjects);
                                }
                                boolean registrationResult =
                                        EnrolleeService.registerEnrollee(enrollee);
                                if (!registrationResult) {
                                    enrollee = null;
                                }
                                session.setAttribute(ENROLLEE, enrollee);
                            } else {
                                enrollee.setCity(city);
                                enrollee.setCountry(country);
                                enrollee.setSchoolCertificate(Integer.parseInt(certificate));
                                if (EnrolleeService.updateEnrollee(enrollee)) {
                                    session.setAttribute(ENROLLEE, enrollee);
                                }
                            }
                        } else {
                            request.setAttribute("profileErrorMessage",
                                    MessageManager.getProperty(
                                    "message.update.error.invalid.input", lang));
                        }
                    }
                } else {
                    request.setAttribute("profileErrorMessage",
                            MessageManager.getProperty(
                                    "message.update.error.wrong.password", lang));
                }
            } else {
                request.setAttribute("profileErrorMessage",
                        MessageManager.getProperty(
                        "message.update.error.invalid.input", lang));
            }
            page = ProfileService.definePage(role);
            router.setPage(page);
            router.setType(Router.Type.REDIRECT);
        } catch (ProjectException e) {
            LOG.error("Profile updating error", e);
            router.setType(Router.Type.ERROR);
            router.setErrorCode(500);
        }
        return router;
    }
}
