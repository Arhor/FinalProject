/*
 * class: SignUpCommand
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
public class SignUpCommand implements ActionCommand {

    @Override
    public Router execute(HttpServletRequest request) {
        String page = ConfigurationManager.getProperty("path.page.registration");
        Router router = new Router();
        router.setPage(page);
        router.setType(Router.Type.FORWARD);
        return router;
    }
}
