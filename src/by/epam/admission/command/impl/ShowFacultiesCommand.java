package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.dao.TransactionHelper;
import by.epam.admission.dao.impl.FacultyDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Faculty;
import by.epam.admission.util.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ShowFacultiesCommand implements ActionCommand {

    @Override
    public Router execute(HttpServletRequest request) {
        String page;
        Router router = new Router();
        List<Faculty> faculties;
        TransactionHelper helper = new TransactionHelper();
        FacultyDao facultyDao = new FacultyDao();
        try {
            helper.startTransaction(facultyDao);
            faculties = facultyDao.findAll();
            request.setAttribute("faculties", faculties);
            page = ConfigurationManager.getProperty("path.page.faculties");
        } catch (ProjectException e) {
            page = ConfigurationManager.getProperty("path.page.error");
        } finally {
            helper.endTransaction();
        }
        router.setPage(page);
        router.setType(Router.Type.FORWARD);
        return router;
    }
}
