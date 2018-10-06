/*
 * class: DeregisterFromFaculty
 */

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

/**
 * @author Burishinets Maxim
 * @version 1.0 01 Oct 2018
 */
public class DeregisterFromFaculty implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(DeregisterFromFaculty.class);

    private static final String PARAM_ENROLLEE_ID = "enrolleeId";
    private static final String PARAM_FACULTY_ID = "facultyId";
    private static final String PARAM_FACULTY = "faculty";
    private static final String PARAM_RESULT = "result";

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) {

        String enrolleeId = request.getParameter(PARAM_ENROLLEE_ID);
        String facultyId = request.getParameter(PARAM_FACULTY_ID);
        facultyId = facultyId.replaceAll("[^0-9]","");

        int eid = Integer.parseInt(enrolleeId);
        int fid = Integer.parseInt(facultyId);

        try {
            boolean result = FacultyService.deregisterFromFaculty(eid, fid);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_FACULTY, fid);
            jsonObject.put(PARAM_RESULT, result);
            response.setContentType("application/json");
            response.getWriter().write(jsonObject.toString());
        } catch (ProjectException | IOException | JSONException e) {
            LOG.error("Deregister from faculty error", e);
            try {
                response.sendError(500);
            } catch (IOException e1) {
                LOG.error("Send error failed", e);
            }
        }
        return null;
    }
}
