package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Enrollee;
import by.epam.admission.model.Faculty;
import by.epam.admission.model.User;
import by.epam.admission.service.EnrolleeService;
import by.epam.admission.service.FacultyService;
import by.epam.admission.service.UserService;
import by.epam.admission.util.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.TreeMap;

public class ShowFacultyStatisticsCommand implements ActionCommand {

    private static final Logger LOG = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        String page;
        int facultyId = Integer.parseInt(request.getParameter("facultyId"));
        try {
            Faculty faculty = FacultyService.findFaculty(facultyId);
            TreeMap<User, TreeMap<Enrollee, String>> resultSet = new TreeMap<>();
            List<Enrollee> enrollees = EnrolleeService.findEnrolleesByFacultyId(facultyId);
            for (Enrollee enrollee : enrollees) {
                User user = UserService.findUserById(enrollee.getUserId());
                String status = EnrolleeService.checkEnrolleeStatus(enrollee.getId(), facultyId);
                resultSet.put(user, new TreeMap<Enrollee, String>(){
                    {
                        put(enrollee, status);
                    }
                });
            }
            request.setAttribute("resultSet", resultSet);
            request.setAttribute("faculty", faculty);
            page = ConfigurationManager.getProperty("path.page.faculty.statistics");
            router.setPage(page);
            router.setType(Router.Type.FORWARD);
        } catch (ProjectException e) {
            LOG.error("An exception occurred during finding faculty statistics", e);
            router.setType(Router.Type.ERROR);
            router.setErrorCode(500);
        }
        return router;
    }
}
