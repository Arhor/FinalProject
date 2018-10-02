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
    SHOW_USERS_PREV (new ShowUsersPrevCommand()),
    SHOW_USERS_NEXT (new ShowUsersNextCommand()),
    ENGLISH (new EnglishCommand()),
    RUSSIAN (new RussianCommand()),
    PROFILE (new ProfileCommand()),
    UPDATE_PROFILE (new UpdateProfileCommand()),
    SHOW_USERS (new ShowUsersCommand()),
    REGISTER_TO_FACULTY (new RegisterToFacultyCommand()),
    CHECK_FACULTY (new CheckFacultyCommand()),
    DEREGISTER_FROM_FACULTY (new DeregisterFromFaculty()),
    CHECK_USERS (new CheckUsersCommand()),
    BLOCK_USER (new BlockUserCommand()),
    UNBLOCK_USER (new UnblockUserCommand()),
    ADD_SUBJECT (new AddSubjectCommand());

    CommandEnum(ActionCommand command) {
        this.command = command;
    }

    private ActionCommand command;

    public ActionCommand getCurrentCommand() {
        return command;
    }
}
