package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class HomeCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request) {
        String page;
        HttpSession session = request.getSession();
        User.Role currentRole = (User.Role) session.getAttribute("role");
        switch (currentRole) {
            case GUEST:
                page = ConfigurationManager.getProperty("path.page.main");
                break;
            case ADMIN: // STUB TODO: implement administrator view!!!
            case CLIENT:
                page = ConfigurationManager.getProperty("path.page.client.main");
                break;
            default:
                page = ConfigurationManager.getProperty("page.path.index");
        }
        return page;
    }
}
