/*
 * class: AddSubjectCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.EnrolleeService;
import by.epam.admission.logic.SubjectService;
import by.epam.admission.model.Enrollee;
import by.epam.admission.model.Subject;
import by.epam.admission.util.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @author Burishinets Maxim
 * @version 1.0 03 Oct 2018
 */
public class AddSubjectCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(AddSubjectCommand.class);

    private static final String ATTR_ENROLLEE = "enrollee";
    private static final String ATTR_AVAILABLE_SUBJECT = "availableSubjects";
    private static final String PARAM_SUBJECT_TO_ADD = "subjectToAdd";
    private static final String PARAM_SUBJECT_SCORE = "subjectScore";

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) {
        Router router = new Router();

        HttpSession session = request.getSession();

        Enrollee enrollee = (Enrollee) session.getAttribute(ATTR_ENROLLEE);
        List<Subject> subjects = (List<Subject>) session.getAttribute(
                ATTR_AVAILABLE_SUBJECT);

        int subjectId = Integer.valueOf(
                request.getParameter(PARAM_SUBJECT_TO_ADD)
                       .replaceAll("[^0-9]", ""));
        int subjectScore = Integer.valueOf(
                request.getParameter(PARAM_SUBJECT_SCORE));

        try {
            boolean result = EnrolleeService.addSubject(
                    enrollee.getId(), subjectId, subjectScore);
            if (result) {
                Subject subject = SubjectService.findSubjectById(subjectId);
                subjects.remove(subject);
                enrollee.getMarks().put(subject, subjectScore);
                session.setAttribute(ATTR_ENROLLEE, enrollee);
                session.setAttribute(ATTR_AVAILABLE_SUBJECT, subjects);
            }
            String page = ConfigurationManager.getProperty(
                    "path.page.client.main");
            router.setPage(page);
            router.setType(Router.Type.FORWARD);
        } catch (ProjectException e) {
            LOG.error(e);
            router.setType(Router.Type.ERROR);
            try {
                response.sendError(500);
            } catch (IOException e1) {
                LOG.error(e1);
            }
        }
        return router;
    }
}
