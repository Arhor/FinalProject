/*
 * class: ActionFactory
 */

package by.epam.admission.command;

import by.epam.admission.command.impl.EmptyCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Burishinets Maxim
 * @version 1.0 02 Sep 2018
 */
public class ActionFactory {

    private static final Logger LOG = LogManager.getLogger(ActionFactory.class);

    private static final String PARAM_COMMAND = "command";

    private ActionFactory() {}

    public static ActionCommand defineCommand(HttpServletRequest request) {
        ActionCommand currentCommand = new EmptyCommand();
        String action = request.getParameter(PARAM_COMMAND);
        if (action == null || action.isEmpty()) {
            currentCommand = CommandEnum.EMPTY_COMMAND.getCurrentCommand();
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
