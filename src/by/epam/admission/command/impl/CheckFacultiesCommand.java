/*
 * class: CheckFacultiesCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.FacultyService;
import by.epam.admission.model.Enrollee;
import by.epam.admission.model.Subject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * @author Burishinets Maxim
 * @version 1.0 20 Sep 2018
 */
public class CheckFacultiesCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(CheckFacultiesCommand.class);

    private static final String ATTR_ENROLLEE = "enrollee";
    private static final String PARAM_FACULTY_ID_ARRAY = "facultyId[]";
    private static final String PARAM_RESULT_SET = "resultSet";

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();

        Enrollee enrollee = (Enrollee) session.getAttribute(ATTR_ENROLLEE);

        int enrolleeId = enrollee.getId();
        Set<Subject> subjects = enrollee.getMarks().keySet();
        String[] facultyIds = request.getParameterValues(PARAM_FACULTY_ID_ARRAY);

        try {
            HashMap<Integer, Boolean> resultSet = FacultyService.checkFaculty(
                    enrolleeId, subjects, facultyIds);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_RESULT_SET, resultSet);
            response.setContentType("application/json");
            response.getWriter().write(jsonObject.toString());
        } catch (ProjectException | IOException | JSONException e) {
            LOG.error("Checking faculties error", e);
            response.sendError(500);
        }
        return null;
    }
}
