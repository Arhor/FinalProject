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
import by.epam.admission.util.Names;
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

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        Router router = new Router();

        HttpSession session = request.getSession();

        Enrollee enrollee = (Enrollee) session.getAttribute(Names.ENROLLEE);
        List<Subject> subjects = (List<Subject>) session.getAttribute(
                Names.AVAILABLE_SUBJECTS);

        int subjectId = Integer.valueOf(
                request.getParameter(Names.SUBJECT_TO_ADD)
                       .replaceAll("[^0-9]", ""));
        int subjectScore = Integer.valueOf(
                request.getParameter(Names.SUBJECT_SCORE));

        try {
            boolean result = EnrolleeService.addSubject(
                    enrollee.getId(), subjectId, subjectScore);
            if (result) {
                Subject subject = SubjectService.findSubjectById(subjectId);
                subjects.remove(subject);
                enrollee.getMarks().put(subject, subjectScore);
                session.setAttribute(Names.ENROLLEE, enrollee);
                session.setAttribute(Names.AVAILABLE_SUBJECTS, subjects);
            }
            String page = ConfigurationManager.getProperty(
                    "path.page.client.main");
            router.setPage(page);
            router.setType(Router.Type.FORWARD);
        } catch (ProjectException e) {
            LOG.error("Add subject error", e);
            router.setType(Router.Type.ERROR);
            response.sendError(500);
        }
        return router;
    }
}
