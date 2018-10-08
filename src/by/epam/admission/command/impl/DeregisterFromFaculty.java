/*
 * class: DeregisterFromFaculty
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

/**
 * @author Burishinets Maxim
 * @version 1.0 01 Oct 2018
 */
public class DeregisterFromFaculty implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(DeregisterFromFaculty.class);

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        String enrolleeId = request.getParameter(Names.ENROLLEE_ID);
        String facultyId = request.getParameter(Names.FACULTY_ID);
        facultyId = facultyId.replaceAll("[^0-9]","");
        int eid = Integer.parseInt(enrolleeId);
        int fid = Integer.parseInt(facultyId);
        try {
            boolean result = FacultyService.deregisterFromFaculty(eid, fid);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Names.FACULTY, fid);
            jsonObject.put(Names.RESULT, result);
            router.setType(Router.Type.AJAX);
            router.setJsonObject(jsonObject);
        } catch (ProjectException | JSONException e) {
            LOG.error("Deregister from faculty error", e);
            router.setType(Router.Type.ERROR);
            router.setErrorCode(500);
        }
        return router;
    }
}
