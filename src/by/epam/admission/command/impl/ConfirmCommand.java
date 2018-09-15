package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.logic.MailLogic;
import by.epam.admission.logic.RegisterLogic;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;
import by.epam.admission.util.MessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ConfirmCommand implements ActionCommand {

    private static final Logger LOG = LogManager.getLogger(ConfirmCommand.class);

    @Override
    public String execute(HttpServletRequest request) {
        String page;
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("user");
        String password = (String) session.getAttribute("password");

        String submittedCode = request.getParameter("confirmationCode");
        String realCode = String.valueOf(session.getAttribute("confirmationCode"));

        boolean equivalency = submittedCode.equals(realCode);

        LOG.debug("submitted code: " + submittedCode);
        LOG.debug("Real code: " + realCode);
        LOG.debug("Equality: " + equivalency);

        if (equivalency) {
            user = RegisterLogic.registerUser(user, password);
            if (user != null) {
                request.setAttribute("user", user.getFirstName() + " " + user.getLastName());
                session.setAttribute("role", user.getRole());
                page = ConfigurationManager.getProperty("path.page.main");
            } else {
                request.setAttribute("errorLoginMessage", MessageManager.getProperty("message.loginerror"));
                page = ConfigurationManager.getProperty("path.page.login");
            }
        } else {
            page = ConfigurationManager.getProperty("path.page.index");
        }


        return page;
    }
}
