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
import java.io.IOException;
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
            if (EnrolleeService.addSubject(enrollee.getId(), subjectId, subjectScore)) {
                Subject subject = SubjectService.findSubjectById(subjectId);
                subjects.remove(subject);
                enrollee.getMarks().put(subject, subjectScore);
                request.getSession().setAttribute("enrollee", enrollee);
                request.getSession().setAttribute("availableSubjects", subjects);

                LOG.debug("SUCCESS");

            }
        } catch (ProjectException e) {
            LOG.error(e);
            LOG.debug(e);
            try {
                response.sendError(500);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }


        router.setPage(ConfigurationManager.getProperty("path.page.client.main"));
        router.setType(Router.Type.FORWARD);
        return router;
    }
}
