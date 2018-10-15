/*
 * class: AjaxServlet
 */

package by.epam.admission.controller;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.ActionFactory;
import by.epam.admission.command.Router;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Class AjaxServlet serves as controller for AJAX request/response
 *
 * @author Burishinets Maxim
 * @version 1.0 15 Sep 2018
 */
@WebServlet(
        name = "AjaxServlet",
        urlPatterns = "/ajaxServlet"
)
public class AjaxServlet extends HttpServlet {

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
     * appropriate commands and placing a JSON object with the result in
     * response
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
        switch (router.getType()) {
            case AJAX:
                JSONObject jsonObject = router.getJsonObject();
                response.setContentType("application/json");
                response.getWriter().write(jsonObject.toString());
                break;
        }
    }

}
