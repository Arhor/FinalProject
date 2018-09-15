package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.LoginLogic;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;
import by.epam.admission.util.MessageManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginCommand implements ActionCommand {

    private static final String PARAM_NAME_EMAIL = "email";
    private static final String PARAM_NAME_PASSWORD = "password";

    @Override
    public String execute(HttpServletRequest request) {
        String page;

        String login = request.getParameter(PARAM_NAME_EMAIL);
        String password = request.getParameter(PARAM_NAME_PASSWORD);

        HttpSession session = request.getSession();

        User user;
        try {
            user = LoginLogic.checkLogin(login, password);
            if(user != null) {
                request.setAttribute("user", user.getFirstName() + " " + user.getLastName());
                session.setAttribute("user", user);
                session.setAttribute("role", user.getRole());
                page = ConfigurationManager.getProperty("path.page.main");
            } else {
                request.setAttribute("errorLoginMessage", MessageManager.getProperty("message.loginerror"));
                page = ConfigurationManager.getProperty("path.page.login");
            }
        } catch (ProjectException e) {
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }

}
