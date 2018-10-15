/*
 * class: DeregisterFromFaculty
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.User;
import by.epam.admission.service.FacultyService;
import by.epam.admission.util.MessageManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

import static by.epam.admission.util.Names.*;

/**
 * Class DeregisterFromFaculty used to deregister invoked it enrollee from
 * concrete faculty
 *
 * @author Burishinets Maxim
 * @version 1.0 01 Oct 2018
 * @see ActionCommand
 */
public class DeregisterFromFaculty implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(DeregisterFromFaculty.class);

    /**
     * Method
     *
     * @param request {@link HttpServletRequest} object received from
     *                controller-servlet
     * @return {@link Router} object that contains result of executing concrete
     * command
     */
    @Override
    public Router execute(HttpServletRequest request) {
        boolean result;
        Router router = new Router();
        JSONObject jsonObject = new JSONObject();
        String locale = (String) request.getSession().getAttribute(LOCALE);
        User.Lang lang = User.Lang.getLang(locale);
        String enrolleeId = request.getParameter(ENROLLEE_ID);
        String facultyId = request.getParameter(FACULTY_ID);
        facultyId = facultyId.replaceAll("[^0-9]","");
        int eid = Integer.parseInt(enrolleeId);
        int fid = Integer.parseInt(facultyId);
        try {
            if (!FacultyService.checkFacultyStatus(fid)) {
                result = FacultyService.deregisterFromFaculty(eid, fid);
                if (!result) {
                    jsonObject.put(MESSAGE, MessageManager.getProperty(
                            "message.faculty.deregistration.failed", lang));
                }
            } else {
                result = false;
                jsonObject.put(MESSAGE, MessageManager.getProperty(
                        "message.faculty.registration.over", lang));
            }
            jsonObject.put(FACULTY, fid);
            jsonObject.put(RESULT, result);
        } catch (ProjectException | JSONException e) {
            LOG.error("Deregister from faculty error", e);
            try {
                jsonObject.put(ERROR, true);
            } catch (JSONException e1) {
                LOG.error("JSON error", e1);
            }
        }
        router.setType(Router.Type.AJAX);
        router.setJsonObject(jsonObject);
        return router;
    }
}
