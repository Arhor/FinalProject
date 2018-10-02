package by.epam.admission.command.factory;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.impl.EmptyCommand;
import by.epam.admission.command.CommandEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class ActionFactory {

    private static final Logger LOG = LogManager.getLogger(ActionFactory.class);

    public ActionCommand defineCommand(HttpServletRequest request) {

        ActionCommand currentCommand = new EmptyCommand();

        String action = request.getParameter("command");

        if (action == null || action.isEmpty()) {
            return currentCommand;
        } else {
            action = action.replace(" ", "_")
                           .replaceAll("[0-9]", "")
                           .toUpperCase();
        }

        try {
            CommandEnum currentEnum = CommandEnum.valueOf(action);
            currentCommand = currentEnum.getCurrentCommand();
        } catch(IllegalArgumentException e) {
            LOG.error("Unknown command: " + action, e);
        }

        return currentCommand;
    }

}
