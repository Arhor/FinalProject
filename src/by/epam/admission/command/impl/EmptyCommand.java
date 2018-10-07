/*
 * class: EmptyCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.util.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Burishinets Maxim
 * @version 1.0 29 Aug 2019
 */
public class EmptyCommand implements ActionCommand {

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        Router router = new Router();
        router.setType(Router.Type.ERROR);
        response.sendError(404);
        return router;
    }
}
