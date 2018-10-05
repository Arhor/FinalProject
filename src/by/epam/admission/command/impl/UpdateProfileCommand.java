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

public class UpdateProfileCommand implements ActionCommand {

    private static final Logger LOG = LogManager.getLogger(UpdateProfileCommand.class);

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) {
        Router router = new Router();

        HttpSession session = request.getSession();

        String firstName = request.getParameter("firstName").replaceAll("</?script>", "");
        String lastName = request.getParameter("lastName").replaceAll("</?script>", "");
        String password = request.getParameter("password");

        User user = (User) session.getAttribute("user");

        if (user.getRole() == User.Role.CLIENT) {
            String city = request.getParameter("city").replaceAll("</?script>", "");
            String country = request.getParameter("country").replaceAll("</?script>", "");
            String certificate = request.getParameter("certificate");
            Enrollee enrollee = (Enrollee) session.getAttribute("enrollee");
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
