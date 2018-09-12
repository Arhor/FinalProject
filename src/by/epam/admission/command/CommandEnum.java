package by.epam.admission.command;

import by.epam.admission.command.impl.*;

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
    },
    SIGN_IN {
        {
            this.command = new SignInCommand();
        }
    },
    SIGN_UP {
        {
            this.command = new SignUpCommand();
        }
    };

    ActionCommand command;

    public ActionCommand getCurrentCommand() {
        return command;
    }
}
