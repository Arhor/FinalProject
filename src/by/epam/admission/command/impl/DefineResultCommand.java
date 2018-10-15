/*
 * class: DefineResultCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Faculty;
import by.epam.admission.service.FacultyService;
import by.epam.admission.util.ConfigurationManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static by.epam.admission.command.Router.*;
import static by.epam.admission.util.Names.*;

/**
 * Class DefineResultCommand used for initialize faculty result definition
 *
 * @author Burishinets Maxim
 * @version 1.0 05 Oct 2018
 * @see ActionCommand
 */
public class DefineResultCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(DefineResultCommand.class);

    /**
     * Method receives faculty ID as request parameter and defines result for
     * the corresponding faculty, after all receives new faculties list and sets
     * it as attribute to request
     *
     * @param request {@link HttpServletRequest} object received from
     *                controller-servlet
     * @return {@link Router} object that contains result of executing concrete
     * command
     */
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        String page;
        List<Faculty> faculties;
        int facultyId = Integer.parseInt(request.getParameter(FACULTY_ID));
        try {
            FacultyService.defineFacultyResult(facultyId);
            faculties = FacultyService.findFaculties();
            request.setAttribute(FACULTIES, faculties);
            page = ConfigurationManager.getProperty("path.page.faculties");
            router.setType(Type.FORWARD);
            router.setPage(page);
        } catch (ProjectException e) {
            LOG.error("Error occurred during definition faculty result", e);
            router.setType(Type.ERROR);
            router.setErrorCode(500);
        }
        return router;
    }
}
