package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.dao.DaoHelper;
import by.epam.admission.dao.impl.FacultyDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.FacultyService;
import by.epam.admission.model.Faculty;
import by.epam.admission.util.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShowFacultiesCommand implements ActionCommand {

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        Router router = new Router();
        List<Faculty> faculties = null;
        try {
            faculties = FacultyService.findFaculties();
            page = ConfigurationManager.getProperty("path.page.faculties");
        } catch (ProjectException e) {
            e.printStackTrace();
            page = ConfigurationManager.getProperty("path.page.error");
        }
        request.setAttribute("faculties", faculties);
        router.setPage(page);
        router.setType(Router.Type.FORWARD);
        return router;
    }
}
