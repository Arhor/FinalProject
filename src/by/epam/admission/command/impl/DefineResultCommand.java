/*
 * class: DefineResultCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Burishinets Maxim
 * @version 1.0 05 Oct 2018
 */
public class DefineResultCommand implements ActionCommand {

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) {
        // TODO: implement faculty admission result
        return null;
    }
}
