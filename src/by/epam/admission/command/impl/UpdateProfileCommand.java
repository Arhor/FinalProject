package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.logic.EnrolleeService;
import by.epam.admission.model.Enrollee;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UpdateProfileCommand implements ActionCommand {

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        String page = ConfigurationManager.getProperty("path.page.client.profile");

        HttpSession session = request.getSession();

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String certificate = request.getParameter("certificate");

        User user = (User) session.getAttribute("user");
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

//        if (firstName == null || lastName == null
//                || firstName.isEmpty() || lastName.isEmpty()) {
//            request.setAttribute("userError",
//                    "User first name and last nam must not be empty");
//        }
//
//        user.setFirstName(firstName);
//        user.setLastName(lastName);


        session.setAttribute("enrollee", enrollee);

        router.setPage(page);
        router.setType(Router.Type.FORWARD);
        return router;
    }
}
