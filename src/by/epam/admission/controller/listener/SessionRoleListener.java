/*
 * class: SessionRoleListener
 */

package by.epam.admission.controller.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import static by.epam.admission.model.User.*;
import static by.epam.admission.util.Names.*;

/**
 * Class SessionRoleListener used to set newly created sessions attribute 'role'
 * to User.Role.GUEST and attribute 'locale' to 'en_US'
 *
 * @author Burishinets Maxim
 * @version 1.0 29 Aug 2018
 * @see javax.servlet.http.HttpSessionListener
 */
@WebListener
public class SessionRoleListener implements HttpSessionListener {

    /**
     * Sets newly created sessions attribute 'role' to User.Role.GUEST and
     * attribute 'locale' to 'en_US'
     *
     * @param httpSessionEvent {@link HttpSessionEvent} object
     */
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        Role role = (Role) session.getAttribute(ROLE);
        if (role == null) {
            session.setAttribute(ROLE, Role.GUEST);
            session.setAttribute(LOCALE, Lang.EN.getValue());
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}
