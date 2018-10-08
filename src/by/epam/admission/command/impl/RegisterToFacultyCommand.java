/*
 * class: RegisterToFacultyCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.FacultyService;
import by.epam.admission.util.Names;
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

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) {
        boolean result;
        String enrolleeId = request.getParameter(Names.ENROLLEE_ID);
        String facultyId = request.getParameter(Names.FACULTY_ID);
        facultyId = facultyId.replaceAll("[^0-9]","");
        int eid = Integer.parseInt(enrolleeId);
        int fid = Integer.parseInt(facultyId);
        try {
            if (FacultyService.checkAdmissionListEntry(eid, fid)) {
                result = FacultyService.restoreFacultyRegistration(eid, fid);
            } else {
                result = FacultyService.registerToFaculty(eid, fid);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Names.FACULTY, fid);
            jsonObject.put(Names.RESULT, result);
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
