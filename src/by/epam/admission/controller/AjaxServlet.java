package by.epam.admission.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "AjaxServlet",
        urlPatterns = "/ajaxServlet"
)
public class AjaxServlet extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(AjaxServlet.class);

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

        String enrolleeId = request.getParameter("command");

        LOG.debug(enrolleeId);

        response.setContentType("text/plain");
        response.getWriter().write(String.valueOf(enrolleeId));
    }

}
