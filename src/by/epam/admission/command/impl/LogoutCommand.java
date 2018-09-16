package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.util.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;

public class LogoutCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request) {
        String page = ConfigurationManager.getProperty("path.page.main");
        request.getSession().invalidate();
        return null; // STUB trying to logout via redirect
    }

}