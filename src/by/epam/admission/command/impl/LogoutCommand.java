/*
 * class: LogoutCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.util.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Burishinets Maxim
 * @version 1.0 05 Sep 2018
 */
public class LogoutCommand implements ActionCommand {

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) {
        String page = ConfigurationManager.getProperty("path.page.main");
        Router router = new Router();
        router.setPage(page);
        router.setType(Router.Type.FORWARD);
        request.getSession().invalidate();
        return router;
        // TODO: prevent return back via 'back' or 'backspace' buttons
    }

}