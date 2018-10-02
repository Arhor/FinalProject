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
import java.util.List;

public class AddSubjectCommand implements ActionCommand {

    private static final Logger LOG = LogManager.getLogger(AddSubjectCommand.class);

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) {
        Router router = new Router();

        Enrollee enrollee = (Enrollee) request.getSession().getAttribute("enrollee");
        List<Subject> subjects = (List<Subject>) request.getSession().getAttribute("availableSubjects");

        int subjectId = Integer.valueOf(
                request.getParameter("subjectToAdd")
                       .replaceAll("[^0-9]", ""));
        int subjectScore = Integer.valueOf(
                request.getParameter("subjectScore"));

        LOG.debug(subjectId);
        LOG.debug(subjectScore);

        try {
            if (EnrolleeService.addSubject(enrollee.getUserId(), subjectId, subjectScore)) {
                Subject subject = SubjectService.findSubjectById(subjectId);
                subjects.remove(subject);
                enrollee.getMarks().put(subject, subjectScore);
                request.getSession().setAttribute("enrollee", enrollee);
                request.getSession().setAttribute("availableSubjects", subjects);
                router.setPage(ConfigurationManager.getProperty("path.page.client.main"));
            }
        } catch (ProjectException e) {
            LOG.error(e);
            router.setPage(ConfigurationManager.getProperty("path.page.error"));
        }



        router.setType(Router.Type.FORWARD);
        return router;
    }
}
