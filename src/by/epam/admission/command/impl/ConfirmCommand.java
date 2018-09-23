package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
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
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("user");
        String password = (String) session.getAttribute("password");

        String submittedCode = request.getParameter("confirmationCode");
        String realCode = String.valueOf(session.getAttribute("confirmationCode"));

        if (submittedCode.equals(realCode)) {
            user = RegisterLogic.registerUser(user, password);
            if (user != null) {
                session.setAttribute("role", user.getRole());
                session.setAttribute("locale", user.getLang().getValue());
                router.setPage(ConfigurationManager.getProperty("path.page.client.main"));
                router.setType(Router.Type.FORWARD);
            } else {
                request.setAttribute("errorLoginMessage", MessageManager.getProperty("message.loginerror"));
                router.setPage(ConfigurationManager.getProperty("path.page.login"));
                router.setType(Router.Type.FORWARD);
            }
        } else {
            session.removeAttribute("user");
            router.setPage(ConfigurationManager.getProperty("path.page.main"));
            router.setType(Router.Type.FORWARD);
        }
        session.removeAttribute("confirmationCode");
        return router;
    }
}
