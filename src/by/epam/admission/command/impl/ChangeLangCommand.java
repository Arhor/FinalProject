/*
 * class: ChangeLangCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.util.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.epam.admission.model.User.*;
import static by.epam.admission.util.Names.*;
import static by.epam.admission.util.Names.EN;
import static by.epam.admission.util.Names.RU;

/**
 * Class ChangeLangCommand is used to change the system language
 *
 * @author Burishinets Maxim
 * @version 1.0 03 Sep 2018
 * @see ActionCommand
 */
public class ChangeLangCommand implements ActionCommand {

    /**
     * Method retrieves HTTP-request parameter 'language' and depending on it
     * replaces session attribute 'locale' with 'ru_RU' or 'en_US', after all,
     * depending on current session's role defines destination page that is
     * placed in {@link Router} object
     *
     * @param request {@link HttpServletRequest} object received from
     *                controller-servlet
     * @return {@link Router} object that contains result of executing concrete
     * command
     */
    @Override
    public Router execute(HttpServletRequest request) {
        String page;
        Router router = new Router();
        HttpSession session = request.getSession();
        String lang = request.getParameter(LANG);
        switch (lang) {
            case RU:
                session.setAttribute(LOCALE, Lang.RU.getValue());
                break;
            case EN:
            default:
                session.setAttribute(LOCALE, Lang.EN.getValue());
        }
        Role currentRole = (Role) session.getAttribute(ROLE);
        switch (currentRole) {
            case GUEST:
                page = ConfigurationManager.getProperty("path.page.main");
                break;
            case ADMIN:
                page = ConfigurationManager.getProperty("path.page.admin.main");
                break;
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
