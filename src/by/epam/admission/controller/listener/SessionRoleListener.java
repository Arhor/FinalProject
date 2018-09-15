package by.epam.admission.controller.listener;

import by.epam.admission.model.User;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionRoleListener implements HttpSessionListener {

    // Sets newly crated session user-role to "GUEST"
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        User.Role role = (User.Role) session.getAttribute("role");
        if (role == null) {
            session.setAttribute("role", User.Role.GUEST);
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}
