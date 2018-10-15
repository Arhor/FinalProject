/*
 * class: EmptyCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;

import javax.servlet.http.HttpServletRequest;

import static by.epam.admission.command.Router.*;

/**
 * Class EmptyCommand used for handling unknown command situations.
 *
 * @author Burishinets Maxim
 * @version 1.0 29 Aug 2019
 */
public class EmptyCommand implements ActionCommand {

    /**
     * Method sets {@link Router} type as ERROR with status code 404
     *
     * @param request {@link HttpServletRequest} object received from
     *                controller-servlet
     * @return {@link Router} object that contains result of executing concrete
     * command
     */
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setType(Type.ERROR);
        router.setErrorCode(404);
        return router;
    }
}
