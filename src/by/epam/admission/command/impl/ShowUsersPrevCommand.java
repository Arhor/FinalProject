package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.dao.DaoHelper;
import by.epam.admission.dao.impl.UserDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShowUsersPrevCommand implements ActionCommand {

    private static final int ROWS_PER_PAGE = 10;

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) {
        String page;
        Router router = new Router();
        List<User> users;
        DaoHelper helper = new DaoHelper();
        UserDao userDao = new UserDao();
        try {
            helper.startTransaction(userDao);
            Integer pageNum = (Integer)
                    request.getSession().getAttribute("pageNum");
            if (pageNum == null) {
                pageNum = 0;
            }
            users = userDao.findAll(--pageNum, ROWS_PER_PAGE);
            int totalUsers = userDao.findTotalAmount();
            int pageMax = (int) (
                    Math.ceil((double) totalUsers / ROWS_PER_PAGE) - 1);
            request.setAttribute("users", users);
            request.getSession().setAttribute("pageNum", pageNum);
            request.getSession().setAttribute("pageMax", pageMax);
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
