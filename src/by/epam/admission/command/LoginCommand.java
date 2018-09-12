package by.epam.admission.command;

import by.epam.admission.logic.LoginLogic;
import by.epam.admission.util.ConfigurationManager;
import by.epam.admission.util.MessageManager;

import javax.servlet.http.HttpServletRequest;

public class LoginCommand implements ActionCommand {

    private static final String PARAM_NAME_LOGIN = "login";
    private static final String PARAM_NAME_PASSWORD = "password";

    @Override
    public String execute(HttpServletRequest request) {
        String page = null;

        String login = request.getParameter(PARAM_NAME_LOGIN);
        String password = request.getParameter(PARAM_NAME_PASSWORD);

        if(LoginLogic.checkLogin(login, password)) {
            request.setAttribute("user", login);
            page = ConfigurationManager.getProperty("path.page.main");
        } else {
            request.setAttribute("errorLoginMessage", MessageManager.getProperty("message.loginerror"));
            page = ConfigurationManager.getProperty("path.page.login");
        }
        return page;
    }

}
