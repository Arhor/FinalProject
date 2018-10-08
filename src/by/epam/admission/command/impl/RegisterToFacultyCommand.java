/*
 * class: RegisterToFacultyCommand
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
 * @version 1.0 10 Sep 2018
 */
public class RegisterToFacultyCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(RegisterToFacultyCommand.class);

    private static final String PARAM_ENROLLEE_ID = "enrolleeId";
    private static final String PARAM_FACULTY_ID = "facultyId";
    private static final String FACULTY = "faculty";
    private static final String RESULT = "result";

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) {
        boolean result;
        String enrolleeId = request.getParameter(PARAM_ENROLLEE_ID);
        String facultyId = request.getParameter(PARAM_FACULTY_ID);
        facultyId = facultyId.replaceAll("[^0-9]","");
        int eid = Integer.parseInt(enrolleeId);
        int fid = Integer.parseInt(facultyId);
        LOG.debug("Enrollee ID: " + eid + "\nFaculty ID: " + fid);
        try {
            LOG.debug("Check Faculty: " + FacultyService.checkAdmissionListEntry(eid, fid));
            if (FacultyService.checkAdmissionListEntry(eid, fid)) {
                LOG.debug("RESTORE REGISTRATION");
                result = FacultyService.restoreFacultyRegistration(eid, fid);
            } else {
                LOG.debug("REGISTER TO FACULTY");
                result = FacultyService.registerToFaculty(eid, fid);
            }
            LOG.debug("RESULT: " + result);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(FACULTY, fid);
            jsonObject.put(RESULT, result);
            response.setContentType("application/json");
            response.getWriter().write(jsonObject.toString());
        } catch (ProjectException | JSONException | IOException e) {
            LOG.error("Registration to faculty error", e);
            try {
                response.sendError(500);
            } catch (IOException e1) {
                LOG.error(e1);
            }
        }
        return null;
    }
}
