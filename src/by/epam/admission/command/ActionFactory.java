/*
 * class: ActionFactory
 */

package by.epam.admission.command;

import by.epam.admission.util.Names;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * ActionFactory class contains factory-method for Command objects
 *
 * @author Burishinets Maxim
 * @version 1.0 02 Sep 2018
 */
public class ActionFactory {

    private static final Logger LOG = LogManager.getLogger(ActionFactory.class);

    private ActionFactory() {}

    /**
     * Factory method that defines concrete type of Command object to return
     *
     * @param request HttpServletRequest object received from
     *                controller-servlet
     * @return concrete implementation of {@link ActionCommand} depending on
     * parameter "command" from received HttpServletRequest object
     */
    public static ActionCommand defineCommand(HttpServletRequest request) {
        ActionCommand currentCommand =
                CommandEnum.EMPTY_COMMAND.getCurrentCommand();
        String action = request.getParameter(Names.COMMAND);
        if (action == null || action.isEmpty()) {
            return currentCommand;
        } else {
            action = action.replace(" ", "_")
                           .replaceAll("[0-9]", "")
                           .toUpperCase();
            try {
                CommandEnum currentEnum = CommandEnum.valueOf(action);
                currentCommand = currentEnum.getCurrentCommand();
            } catch(IllegalArgumentException e) {
                LOG.error("Unknown command: " + action, e);
            }
        }
        return currentCommand;
    }

}
