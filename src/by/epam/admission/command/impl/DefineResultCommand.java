/*
 * class: DefineResultCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.FacultyService;
import by.epam.admission.util.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Burishinets Maxim
 * @version 1.0 05 Oct 2018
 */
public class DefineResultCommand implements ActionCommand {

    private static final Logger LOG = LogManager.getLogger(DefineResultCommand.class);

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        String page = ConfigurationManager.getProperty("path.page.faculties");

        int facultyId = Integer.parseInt(request.getParameter("facultyId"));
        try {
            FacultyService.defineFacultyResult(facultyId);
            router.setType(Router.Type.FORWARD);
            router.setPage(page);
        } catch (ProjectException e) {
            LOG.debug("Error occurred during definition faculty result", e);
            router.setType(Router.Type.ERROR);
            router.setErrorCode(500);
        }
        return router;
    }
}
