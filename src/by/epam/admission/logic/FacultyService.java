package by.epam.admission.logic;

import by.epam.admission.dao.DaoHelper;
import by.epam.admission.dao.impl.EnrolleeDao;
import by.epam.admission.dao.impl.FacultyDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Enrollee;
import by.epam.admission.model.Faculty;

import java.util.List;

public class FacultyService {

    public static List<Faculty> findFaculties() throws ProjectException {
        List<Faculty> faculties = null;
        DaoHelper helper = new DaoHelper();
        FacultyDao facultyDao = new FacultyDao();
        try {
            helper.startTransaction(facultyDao);
            faculties = facultyDao.findAll();
        } catch (ProjectException e) {
            throw e;
        } finally {
            helper.endTransaction();
        }
        return faculties;
    }

    public static boolean registerToFaculty(Enrollee enrollee, int facultyId)
            throws ProjectException {
        boolean result = false;
        DaoHelper helper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        try {
            helper.startTransaction(enrolleeDao);
            result = enrolleeDao.registerToFacultyById(enrollee, facultyId);
        } catch (ProjectException e) {
            throw e;
        } finally {
            helper.endTransaction();
        }
        return result;
    }

    public static boolean checkFaculty(int enrolleeId, int facultyId)
            throws ProjectException {
        boolean result = false;
        DaoHelper helper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        try {
            helper.startTransaction(enrolleeDao);
            result = enrolleeDao.checkFaculty(enrolleeId, facultyId);
        } catch (ProjectException e) {
            throw e;
        } finally {
            helper.endTransaction();
        }
        return result;
    }

}
