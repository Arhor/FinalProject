/*
 * class: ProfileCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.service.ProfileService;
import by.epam.admission.model.User;
import by.epam.admission.util.Names;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Burishinets Maxim
 * @version 1.0 10 Sep 2018
 */
public class ProfileCommand implements ActionCommand {

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        String page;
        HttpSession session = request.getSession();
        User.Role role = (User.Role) session.getAttribute(Names.ROLE);
        page = ProfileService.definePage(role);
        router.setPage(page);
        router.setType(Router.Type.FORWARD);
        return router;
    }
}
