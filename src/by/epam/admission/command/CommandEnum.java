package by.epam.admission.command;

import by.epam.admission.command.impl.*;

public enum CommandEnum {

    SIGN_IN {
        {
            this.command = new SignInCommand();
        }
    },
    SIGN_UP {
        {
            this.command = new SignUpCommand();
        }
    },
    REGISTRATION {
        {
            this.command = new RegisterCommand();
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
    HOME {
        {
            this.command = new HomeCommand();
        }
    },
    CONFIRM {
        {
            this.command = new ConfirmCommand();
        }
    };

    ActionCommand command;

    public ActionCommand getCurrentCommand() {
        return command;
    }
}
