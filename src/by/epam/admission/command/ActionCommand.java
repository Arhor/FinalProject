package by.epam.admission.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ActionCommand {
    Router execute(HttpServletRequest request, HttpServletResponse response);
}
