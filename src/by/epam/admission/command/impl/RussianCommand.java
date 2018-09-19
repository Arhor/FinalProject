package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RussianCommand implements ActionCommand {

    private static final String LOCALE = "ru_RU";

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        String page;
        HttpSession session = request.getSession();
        session.setAttribute("locale", LOCALE);
        User.Role currentRole = (User.Role) session.getAttribute("role");
        switch (currentRole) {
            case GUEST:
                page = ConfigurationManager.getProperty("path.page.main");
                break;
            case ADMIN:
                // TODO: implement admin main page
            case CLIENT:
                page = ConfigurationManager.getProperty("path.page.client.main");
                break;
            default:
                page = ConfigurationManager.getProperty("path.page.index");
        }
        router.setPage(page);
        router.setType(Router.Type.FORWARD);
        return router;
    }
}
