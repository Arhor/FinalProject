/*
 * class: AddSubjectCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.command.Router.Type;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.service.EnrolleeService;
import by.epam.admission.service.SubjectService;
import by.epam.admission.model.Enrollee;
import by.epam.admission.model.Subject;
import by.epam.admission.util.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static by.epam.admission.util.Names.*;

/**
 * Class AddSubjectCommand implements command that signals to add subject and
 * corresponding score to database for enrollee that invoked it
 * @author Burishinets Maxim
 * @version 1.0 03 Oct 2018
 */
public class AddSubjectCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(AddSubjectCommand.class);

    /**
     * Method retrieves Enrollee attribute and List of available for current
     * enrollee subjects, also subject ID and subject score from user input.
     * After that, an attempt is made to add the corresponding entry to the
     * database. If attempt was successful subject removed form the list of
     * available subjects and added to enrollee's Subject/Score map, after that
     * list sets as session attribute.
     *
     * @param request - HttpServletRequest object received from controller-servlet
     * @return Router object that contains result of executing concrete command
     */
    @SuppressWarnings("unchecked")
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        HttpSession session = request.getSession();
        Enrollee enrollee = (Enrollee) session.getAttribute(ENROLLEE);
        List<Subject> subjects
                = (List<Subject>) session.getAttribute(AVAILABLE_SUBJECTS);
        int subjectId = Integer.valueOf(
                request.getParameter(SUBJECT_TO_ADD)
                       .replaceAll("[^0-9]", ""));
        int subjectScore = Integer.valueOf(
                request.getParameter(SUBJECT_SCORE));
        try {
            boolean result = EnrolleeService.addSubject(
                    enrollee.getId(), subjectId, subjectScore);
            if (result) {
                Subject subject = SubjectService.findSubjectById(subjectId);
                subjects.remove(subject);
                enrollee.getMarks().put(subject, subjectScore);
                session.setAttribute(ENROLLEE, enrollee);
                session.setAttribute(AVAILABLE_SUBJECTS, subjects);
            }
            String page = ConfigurationManager.getProperty(
                    "path.page.client.main");
            router.setPage(page);
            router.setType(Type.FORWARD);
        } catch (ProjectException e) {
            LOG.error("Add subject error", e);
            router.setType(Type.ERROR);
            router.setErrorCode(500);
        }
        return router;
    }
}
