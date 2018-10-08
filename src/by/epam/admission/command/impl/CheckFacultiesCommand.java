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
import by.epam.admission.util.Names;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author Burishinets Maxim
 * @version 1.0 20 Sep 2018
 */
public class CheckFacultiesCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(CheckFacultiesCommand.class);

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        HttpSession session = request.getSession();
        Enrollee enrollee = (Enrollee) session.getAttribute(Names.ENROLLEE);
        int enrolleeId = enrollee.getId();
        Set<Subject> subjects = enrollee.getMarks().keySet();
        String[] facultyIds = request.getParameterValues(Names.FACULTY_ID_ARRAY);
        try {
            HashMap<Integer, Boolean> resultSet = FacultyService.checkFaculties(
                    enrolleeId, subjects, facultyIds);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Names.RESULT_SET, resultSet);
            router.setType(Router.Type.AJAX);
            router.setJsonObject(jsonObject);
        } catch (ProjectException | JSONException e) {
            LOG.error("Checking faculties error", e);
            router.setType(Router.Type.ERROR);
            router.setErrorCode(500);
        }
        return router;
    }
}
