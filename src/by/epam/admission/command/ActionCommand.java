/*
 * class: ActionCommand
 */

package by.epam.admission.command;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface for command layer
 * @author Burishinets Maxim
 * @version 1.0 03 Sep 2018
 */
public interface ActionCommand {
    /**
     * The method refers to different services depending on the specific
     * implementation of the command.
     *
     * @param request - HttpServletRequest object received from controller-servlet
     * @return  Router object that contains result of executing concrete command
     */
    Router execute(HttpServletRequest request);
}
