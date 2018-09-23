package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.dao.TransactionHelper;
import by.epam.admission.dao.impl.UserDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ShowUsersCommand implements ActionCommand {

    @Override
    public Router execute(HttpServletRequest request) {
        String page;
        Router router = new Router();
        List<User> users;
        TransactionHelper helper = new TransactionHelper();
        UserDao userDao = new UserDao();
        try {
            helper.startTransaction(userDao);
            users = userDao.findAll();
            request.setAttribute("users", users);
            page = ConfigurationManager.getProperty("path.page.admin.users");
        } catch (ProjectException e) {
            page = ConfigurationManager.getProperty("path.page.error");
        } finally {
            helper.endTransaction();
        }
        router.setPage(page);
        router.setType(Router.Type.FORWARD);
        return router;
    }
}