/*
 * class: ShowFacultiesCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.service.FacultyService;
import by.epam.admission.model.Faculty;
import by.epam.admission.util.ConfigurationManager;
import by.epam.admission.util.Names;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Burishinets Maxim
 * @version 1.0 11 Sep 2018
 */
public class ShowFacultiesCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(ShowFacultiesCommand.class);

    @Override
    public Router execute(HttpServletRequest request) {
        String page;
        Router router = new Router();
        List<Faculty> faculties;
        try {
            faculties = FacultyService.findFaculties();
            page = ConfigurationManager.getProperty("path.page.faculties");
            request.setAttribute(Names.FACULTIES, faculties);
            router.setPage(page);
            router.setType(Router.Type.FORWARD);
        } catch (ProjectException e) {
            LOG.error("An error occurred during finding faculties", e);
            router.setType(Router.Type.ERROR);
            router.setErrorCode(500);
        }
        return router;
    }
}
