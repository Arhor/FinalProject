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
    SHOW_FACULTIES_PREV (new ShowFacultiesPrevCommand()),
    SHOW_FACULTIES_NEXT (new ShowFacultiesNextCommand()),
    ENGLISH (new EnglishCommand()),
    RUSSIAN (new RussianCommand()),
    PROFILE (new ProfileCommand()),
    UPDATE_PROFILE (new UpdateProfileCommand()),
    SHOW_USERS (new ShowUsersCommand()),
    REGISTER_TO_FACULTY (new RegisterToFacultyCommand()),
    CHECK_FACULTY (new CheckFacultyCommand()),
    DEREGISTER_FROM_FACULTY (new DeregisterFromFaculty());

    CommandEnum(ActionCommand command) {
        this.command = command;
    }

    private ActionCommand command;

    public ActionCommand getCurrentCommand() {
        return command;
    }
}
