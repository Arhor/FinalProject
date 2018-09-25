package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.logic.EnrolleeService;
import by.epam.admission.logic.UserService;
import by.epam.admission.model.Enrollee;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UpdateProfileCommand implements ActionCommand {

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) {
        Router router = new Router();

        HttpSession session = request.getSession();

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String password = request.getParameter("password");

        User user = (User) session.getAttribute("user");

        if (user.getRole() == User.Role.CLIENT) {
            String city = request.getParameter("city");
            String country = request.getParameter("country");
            String certificate = request.getParameter("certificate");
            Enrollee enrollee = (Enrollee) session.getAttribute("enrollee");
            if (enrollee == null) {
                enrollee = new Enrollee();
                enrollee.setUserId(user.getId());
                enrollee.setCity(city);
                enrollee.setCountry(country);
                enrollee.setSchoolCertificate(Integer.parseInt(certificate));
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
