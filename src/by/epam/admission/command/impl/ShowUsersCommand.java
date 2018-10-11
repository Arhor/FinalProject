/*
 * class: ShowUsersCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.service.UserService;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;
import by.epam.admission.util.Names;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Burishinets Maxim
 * @version 1.0 20 Sep 2018
 */
public class ShowUsersCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(ShowUsersCommand.class);

    private static final int ROWS_PER_PAGE = 10;

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        HttpSession session = request.getSession();
        User.Role role = (User.Role) session.getAttribute(Names.ROLE);
        if (role == User.Role.ADMIN) {
            try {
                String page;
                List<User> users;
                Integer pageNum = (Integer)
                        request.getSession().getAttribute(Names.PAGE_NUM);
                if (pageNum == null) {
                    pageNum = 0;
                }
                String targetPage = request.getParameter(Names.TARGET_PAGE);
                switch (targetPage) {
                    case "next":
                        pageNum++;
                        break;
                    case "prev":
                        pageNum--;
                        break;
                    case "first":
                        pageNum = 0;
                        break;
                }
                users = UserService.findUsers(pageNum, ROWS_PER_PAGE);
                int totalUsers = UserService.findTotalUsersAmount();
                int pageMax = (int) (Math.ceil(
                        (double) totalUsers / ROWS_PER_PAGE) - 1);
                request.setAttribute(Names.USERS, users);
                request.getSession().setAttribute(Names.PAGE_NUM, pageNum);
                request.getSession().setAttribute(Names.PAGE_MAX, pageMax);
                page = ConfigurationManager.getProperty("path.page.admin.users");
                router.setPage(page);
                router.setType(Router.Type.FORWARD);
            } catch (ProjectException e) {
                LOG.error("Finding users error", e);
                router.setType(Router.Type.ERROR);
                router.setErrorCode(500);
            }
        } else {
            LOG.error("Invalid user role");
            router.setType(Router.Type.ERROR);
            router.setErrorCode(403);
        }
        return router;
    }
}