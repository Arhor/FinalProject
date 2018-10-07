/*
 * class: AjaxServlet
 */

package by.epam.admission.controller;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.factory.ActionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
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

    private void processRequest(HttpServletRequest request,
                                HttpServletResponse response)
            throws ServletException, IOException {

        ActionFactory client = new ActionFactory();
        ActionCommand command = client.defineCommand(request);

        command.execute(request, response);
    }

}
