package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.util.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;

public class StartCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request) {
        return ConfigurationManager.getProperty("path.page.main");
    }
}
