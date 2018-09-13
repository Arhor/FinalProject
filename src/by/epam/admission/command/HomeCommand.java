package by.epam.admission.command;

import by.epam.admission.util.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;

public class HomeCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request) {
        String page = ConfigurationManager.getProperty("path.page.index");
        return page;
    }
}
