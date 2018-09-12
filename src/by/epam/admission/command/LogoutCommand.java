package by.epam.admission.command;

import by.epam.admission.util.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;

public class LogoutCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request) {
        String page = ConfigurationManager.getProperty("path.page.login");
        request.getSession().invalidate();
        return page;
    }

}