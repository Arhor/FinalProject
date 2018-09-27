package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.FacultyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;

public class CheckFacultyCommand implements ActionCommand {

    private static final Logger LOG = LogManager.getLogger(CheckFacultyCommand.class);

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) {

        LOG.debug("START");

        String enrolleeId = request.getParameter("enrolleeId");
        String[] facultyIds = request.getParameterValues("facultyId[]");

        try {
            HashMap<Integer, Boolean> resultSet = FacultyService.checkFaculty(enrolleeId, facultyIds);

            LOG.debug(resultSet);

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("resultSet", resultSet);
                response.setContentType("application/json");
                response.getWriter().write(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (ProjectException e) {
            e.printStackTrace(); // TODO: STUB
        } catch (IOException e) {
            e.printStackTrace(); // TODO: STUB
        }

        return null; // STUB
    }
}
