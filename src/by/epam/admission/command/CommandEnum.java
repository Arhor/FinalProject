/*
 * class: CommandEnum
 */

package by.epam.admission.command;

import by.epam.admission.command.impl.*;

/**
 * @author Burishinets Maxim
 * @version 1.0 03 Sep 2018
 */
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
    CHANGE_LANG (new ChangeLangCommand()),
    PROFILE (new ProfileCommand()),
    UPDATE_PROFILE (new UpdateProfileCommand()),
    SHOW_USERS (new ShowUsersCommand()),
    REGISTER_TO_FACULTY (new RegisterToFacultyCommand()),
    CHECK_FACULTIES (new CheckFacultiesCommand()),
    DEREGISTER_FROM_FACULTY (new DeregisterFromFaculty()),
    CHECK_USERS (new CheckUsersCommand()),
    BLOCK_USER (new BlockUserCommand()),
    UNBLOCK_USER (new UnblockUserCommand()),
    ADD_SUBJECT (new AddSubjectCommand()),
    DEFINE_RESULT (new DefineResultCommand()),
    EMPTY_COMMAND (new EmptyCommand());

    private ActionCommand command;

    CommandEnum(ActionCommand command) {
        this.command = command;
    }

    public ActionCommand getCurrentCommand() {
        return command;
    }
}
