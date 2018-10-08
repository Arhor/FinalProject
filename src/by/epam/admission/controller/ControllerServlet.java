/*
 * class: ControllerServlet
 */

package by.epam.admission.controller;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.command.ActionFactory;
import by.epam.admission.pool.ConnectionPool;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Burishinets Maxim
 * @version 1.0 29 Aug 2018
 */
@WebServlet(
        name = "ControllerServlet",
        urlPatterns = {
                "/controller",
                "/admission",
                "/admission/registration",
                "/admission/authentication"
        }
)
public class ControllerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request,
                                HttpServletResponse response)
            throws ServletException, IOException {

        ActionCommand command = ActionFactory.defineCommand(request);
        Router router = command.execute(request);
        String page = router.getPage();
        switch (router.getType()) {
            case FORWARD:
                RequestDispatcher dispatcher =
                        getServletContext().getRequestDispatcher(page);
                dispatcher.forward(request, response);
                break;
            case REDIRECT:
                response.sendRedirect(request.getContextPath() + page);
                break;
            case ERROR:
                int errorCode = router.getErrorCode();
                response.sendError(errorCode);
            default:
                // TODO: implement impossible type
        }
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void destroy() {
        AbandonedConnectionCleanupThread.checkedShutdown();
        ConnectionPool.POOL.closePool();
    }
}
