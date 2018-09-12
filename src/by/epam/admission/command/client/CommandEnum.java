package by.epam.admission.command.client;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.LoginCommand;
import by.epam.admission.command.LogoutCommand;
import by.epam.admission.command.RegistrationCommand;

public enum CommandEnum {

    REGISTRATION {
        {
            this.command = new RegistrationCommand();
        }
    },

    LOGIN {
        {
            this.command = new LoginCommand();
        }
    },
    LOGOUT {
        {
            this.command = new LogoutCommand();
        }
    };

    ActionCommand command;

    public ActionCommand getCurrentCommand() {
        return command;
    }
}
