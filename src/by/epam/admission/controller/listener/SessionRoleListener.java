/*
 * class: SessionRoleListener
 */

package by.epam.admission.controller.listener;

import by.epam.admission.model.User;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author Burishinets Maxim
 * @version 1.0 29 Aug 2018
 */
@WebListener
public class SessionRoleListener implements HttpSessionListener {

    private static final String ATTR_ROLE = "role";
    private static final String ATTR_LOCALE = "locale";

    // Sets newly crated session user-role to "GUEST"
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        User.Role role = (User.Role) session.getAttribute(ATTR_ROLE);
        if (role == null) {
            session.setAttribute(ATTR_ROLE, User.Role.GUEST);
            session.setAttribute(ATTR_LOCALE, User.Lang.EN.getValue());
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}
