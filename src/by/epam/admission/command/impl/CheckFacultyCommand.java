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
import java.io.IOException;
import java.util.*;

public class CheckFacultyCommand implements ActionCommand {

    private static final Logger LOG = LogManager.getLogger(CheckFacultyCommand.class);

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) {

        Enrollee enrollee = (Enrollee) request.getSession().getAttribute("enrollee");

        int enrolleeId = enrollee.getId();
        Set<Subject> subjects = enrollee.getMarks().keySet();
        String[] facultyIds = request.getParameterValues("facultyId[]");

        LOG.debug(subjects);

        try {
            HashMap<Integer, Boolean> resultSet =
                    FacultyService.checkFaculty(enrolleeId, subjects, facultyIds);

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("resultSet", resultSet);
                response.setContentType("application/json");
                response.getWriter().write(jsonObject.toString());
            } catch (JSONException e) {
                LOG.debug(e);
            }

        } catch (ProjectException | IOException e) {
            LOG.debug(e); // TODO: STUB
        }

        return null; // STUB
    }
}
