package by.epam.admission.command;

import by.epam.admission.command.impl.*;

public enum CommandEnum {

    SIGN_IN (new SignInCommand()),
    SIGN_UP (new SignUpCommand()),
    REGISTER (new RegisterCommand()),
    LOGIN (new LoginCommand()),
    LOGOUT (new LogoutCommand()),
    HOME (new HomeCommand()),
    CONFIRM (new ConfirmCommand()),
    START (new StartCommand()),
    SHOW_FACULTIES (new ShowFacultiesCommand()),
    ENGLISH (new EnglishCommand()),
    RUSSIAN (new RussianCommand()),
    PROFILE (new ProfileCommand()),
    UPDATE_PROFILE (new UpdateProfileCommand()),
    SHOW_USERS (new ShowUsersCommand());

    CommandEnum(ActionCommand command) {
        this.command = command;
    }

    private ActionCommand command;

    public ActionCommand getCurrentCommand() {
        return command;
    }
}
