package by.epam.admission.command.factory;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.impl.EmptyCommand;
import by.epam.admission.command.client.CommandEnum;
import by.epam.admission.util.MessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class ActionFactory {

    private static final Logger LOG = LogManager.getLogger(ActionFactory.class);

    public ActionCommand defineCommand(HttpServletRequest request) {

        ActionCommand current = new EmptyCommand();

        String action = request.getParameter("command").replace(" ", "_").toUpperCase();

        LOG.debug("command: " + action);

        if (action == null || action.isEmpty()) {

            return current;
        }
        try {
            CommandEnum currentEnum = CommandEnum.valueOf(action);

            LOG.debug("currentEnum: " + currentEnum);

            current = currentEnum.getCurrentCommand();
        } catch(IllegalArgumentException e) {
            LOG.debug("error: " + e);
            request.setAttribute("wrongAction", action + " " + MessageManager.getProperty("message.wrongaction"));
        }
        return current;
    }

}
