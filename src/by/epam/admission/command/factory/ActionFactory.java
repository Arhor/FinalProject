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

        String action = request.getParameter("command")
                               .replace(" ", "_")
                               .toUpperCase();

        if (action == null || action.isEmpty()) {
            return currentCommand;
        }

        try {
            CommandEnum currentEnum = CommandEnum.valueOf(action);
            currentCommand = currentEnum.getCurrentCommand();
        } catch(IllegalArgumentException e) {
            LOG.error("Unknown command: " + action, e);
        }

        return currentCommand;
    }

//    public static Optional<ActionCommand> getCommand(String commandName) {
//        return Arrays.stream(CommandEnum.values())
//                     .filter(o -> o.name().equalsIgnoreCase(commandName))
//                     .map(CommandEnum::getCurrentCommand)
//                     .findAny();
//        // Optional.ofNullable(type.orElse(null).getCommand());
//    }

}
