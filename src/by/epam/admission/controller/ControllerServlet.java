package by.epam.admission.controller;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.command.factory.ActionFactory;
import by.epam.admission.model.User;
import by.epam.admission.pool.ConnectionPool;
import by.epam.admission.util.ConfigurationManager;
import by.epam.admission.util.MessageManager;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(
        name = "ControllerServlet",
        urlPatterns = "/controller"
)
public class ControllerServlet extends HttpServlet {

    private static final Logger LOG =
            LogManager.getLogger(ControllerServlet.class);

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
        Router router;

        ActionFactory client = new ActionFactory();
        ActionCommand command = client.defineCommand(request);

        User.Role role = (User.Role) request.getSession().getAttribute("role");
        LOG.debug("current role: " + role);
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            LOG.debug(key + " : " + request.getHeader(key));
        }

        router = command.execute(request);

        switch (router.getType()) {
            case FORWARD:
                RequestDispatcher dispatcher =
                        getServletContext().getRequestDispatcher(router.getPage());
                dispatcher.forward(request, response);
                break;
            case REDIRECT:
                response.sendRedirect(request.getContextPath() + router.getPage());
                break;
            default:
        }
    }

    public void destroy() {
        AbandonedConnectionCleanupThread.checkedShutdown();
        ConnectionPool.POOL.closePool();
    }
}
