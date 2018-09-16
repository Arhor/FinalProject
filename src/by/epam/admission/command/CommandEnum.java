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
    REGISTER {
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
    },
    START {
        {
            this.command = new StartCommand();
        }
    },
    SHOW_FACULTIES {
        {
            this.command = new ShowFacultiesCommand();
        }
    };

    ActionCommand command;

    public ActionCommand getCurrentCommand() {
        return command;
    }
}
