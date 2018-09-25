package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ProfileCommand implements ActionCommand {

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) {
        Router router = new Router();
        String page;
        HttpSession session = request.getSession();
        User.Role role = (User.Role) session.getAttribute("role");
        switch (role) {
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
