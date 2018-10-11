/*
 * class: RegisterToFacultyCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.service.FacultyService;
import by.epam.admission.util.Names;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Burishinets Maxim
 * @version 1.0 10 Sep 2018
 */
public class RegisterToFacultyCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(RegisterToFacultyCommand.class);

    @Override
    public Router execute(HttpServletRequest request) {
        boolean result;
        Router router = new Router();
        JSONObject jsonObject = new JSONObject();
        String enrolleeId = request.getParameter(Names.ENROLLEE_ID);
        String facultyId = request.getParameter(Names.FACULTY_ID);
        facultyId = facultyId.replaceAll("[^0-9]","");
        int eid = Integer.parseInt(enrolleeId);
        int fid = Integer.parseInt(facultyId);
        try {
            if (!FacultyService.checkFacultyStatus(fid)) {
                if (FacultyService.checkAdmissionListEntry(eid, fid)) {
                    result = FacultyService.restoreFacultyRegistration(eid, fid);
                } else {
                    result = FacultyService.registerToFaculty(eid, fid);
                }
                if (!result) {
                    jsonObject.put("message", "faculty registration failed");
                }
            } else {
                result = false;
                jsonObject.put("message", "faculty registration is closed");
            }
            jsonObject.put(Names.FACULTY, fid);
            jsonObject.put(Names.RESULT, result);
        } catch (ProjectException | JSONException e) {
            LOG.error("Registration to faculty error", e);
            try {
                jsonObject.put("error", true);
            } catch (JSONException e1) {
                LOG.error("JSON error", e1);
            }
        }
        router.setType(Router.Type.AJAX);
        router.setJsonObject(jsonObject);
        return router;
    }
}
