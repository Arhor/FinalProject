/*
 * class: ShowUsersCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.UserService;
import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @author Burishinets Maxim
 * @version 1.0 20 Sep 2018
 */
public class ShowUsersCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(ShowUsersCommand.class);

    private static final int ROWS_PER_PAGE = 10;
    private static final String ATTR_USERS = "users";
    private static final String ATTR_ROLE = "role";
    private static final String ATTR_PAGE_NUM = "pageNum";
    private static final String ATTR_PAGE_MAX = "pageMax";
    private static final String PARAM_TARGET_PAGE = "target_page";

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        Router router = new Router();
        HttpSession session = request.getSession();
        User.Role role = (User.Role) session.getAttribute(ATTR_ROLE);
        if (role == User.Role.ADMIN) {
            try {
                String page;
                List<User> users;
                Integer pageNum = (Integer)
                        request.getSession().getAttribute(ATTR_PAGE_NUM);
                if (pageNum == null) {
                    pageNum = 0;
                }
                String targetPage = request.getParameter(PARAM_TARGET_PAGE);
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
                request.setAttribute(ATTR_USERS, users);
                request.getSession().setAttribute(ATTR_PAGE_NUM, pageNum);
                request.getSession().setAttribute(ATTR_PAGE_MAX, pageMax);
                page = ConfigurationManager.getProperty("path.page.admin.users");
                router.setPage(page);
                router.setType(Router.Type.FORWARD);
            } catch (ProjectException e) {
                LOG.error("Finding users error", e);
                router.setType(Router.Type.ERROR);
                response.sendError(500);
            }
        } else {
            LOG.error("Invalid user role");
            router.setType(Router.Type.ERROR);
            response.sendError(403);
        }
        return router;
    }
}