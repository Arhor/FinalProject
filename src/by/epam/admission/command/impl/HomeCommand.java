/*
 * class: HomeCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Burishinets Maxim
 * @version 1.0 03 Sep 2018
 */
public class HomeCommand implements ActionCommand {

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) {
        String page;
        Router router = new Router();
        HttpSession session = request.getSession();
        User.Role currentRole = (User.Role) session.getAttribute("role");
        switch (currentRole) {
            case GUEST:
                page = ConfigurationManager.getProperty("path.page.main");
                break;
            case ADMIN:
                page = ConfigurationManager.getProperty("path.page.admin.main");
                break;
            case CLIENT:
                page = ConfigurationManager.getProperty("path.page.client.main");
                break;
            default:
                page = ConfigurationManager.getProperty("page.path.index");
        }
        router.setPage(page);
        router.setType(Router.Type.FORWARD);
        return router;
    }
}
