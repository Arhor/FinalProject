/*
 * class: CheckFacultiesCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.service.FacultyService;
import by.epam.admission.model.Enrollee;
import by.epam.admission.model.Subject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

import static by.epam.admission.util.Names.*;

/**
 * Class CheckFacultiesCommand serves for checking faculty availability for
 * concrete enrollee depending on it's ID
 *
 * @author Burishinets Maxim
 * @version 1.0 20 Sep 2018
 * @see ActionCommand
 */
public class CheckFacultiesCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(CheckFacultiesCommand.class);

    /**
     * Method retrieves session attribute 'enrollee' and array of faculty ID
     * from the page where the command was invoked, defines availability of
     * concrete faculty for current enrollee by it's subjects. The result is
     * placed in JSON object and encapsulated into Router object
     *
     * @param request {@link HttpServletRequest} object received from
     *               controller-servlet
     * @return {@link Router} object that contains result of executing concrete
     * command
     */
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        JSONObject jsonObject = new JSONObject();
        HttpSession session = request.getSession();
        Enrollee enrollee = (Enrollee) session.getAttribute(ENROLLEE);
        int enrolleeId = enrollee.getId();
        Set<Subject> subjects = enrollee.getMarks().keySet();
        String[] facultyIds = request.getParameterValues(FACULTY_ID_ARRAY);
        try {
            HashMap<Integer, Boolean> resultSet = FacultyService.checkFaculties(
                    enrolleeId, subjects, facultyIds);
            jsonObject.put(RESULT_SET, resultSet);
        } catch (ProjectException | JSONException e) {
            LOG.error("Checking faculties error", e);
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
