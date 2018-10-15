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
 * Class ControllerServlet serves as main controller for whole application
 *
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

    /**
     * Method implements a unified way of processing messages by calling the
     * appropriate commands and depending on the result, it forwards the
     * request further or redirects the user
     *
     * @param request - Extends the ServletRequest interface to provide request
     *               information for HTTP servlets
     * @param response - Extends the ServletResponse interface to provide
     *                HTTP-specific functionality in sending a response
     * @throws ServletException - Defines a general exception a servlet can
     * throw when it encounters difficulty
     * @throws IOException Signals that an I/O exception of some sort has
     * occurred
     */
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
                response.sendError(500);
        }
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**
     * closes existing database connections before shutting down the application
     */
    public void destroy() {
        AbandonedConnectionCleanupThread.checkedShutdown();
        ConnectionPool.POOL.closePool();
    }
}
