package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.util.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;

public class SignUpCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request) {
        String page = ConfigurationManager.getProperty("path.page.registration");
        return page;
    }
}
