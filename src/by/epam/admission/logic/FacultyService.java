package by.epam.admission.logic;

import by.epam.admission.dao.DaoHelper;
import by.epam.admission.dao.impl.EnrolleeDao;
import by.epam.admission.dao.impl.FacultyDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Enrollee;
import by.epam.admission.model.Faculty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FacultyService {

    private static final Logger LOG = LogManager.getLogger(FacultyService.class);

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

    public static boolean registerToFaculty(int enrolleeId, int facultyId)
            throws ProjectException {
        boolean result = false;
        DaoHelper helper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        try {
            helper.startTransaction(enrolleeDao);
            result = enrolleeDao.registerToFacultyById(enrolleeId, facultyId);
        } catch (ProjectException e) {
            throw e;
        } finally {
            helper.endTransaction();
        }
        return result;
    }

    public static HashMap<Integer, Boolean> checkFaculty(String enrolleeId, String[] facultyIds)
            throws ProjectException {
        HashMap<Integer, Boolean> resultSet = new HashMap<>();
        DaoHelper helper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        try {
            helper.startTransaction(enrolleeDao);
            int eid = Integer.parseInt(enrolleeId);

            for (String facultyId : facultyIds) {
                int fid = Integer.parseInt(facultyId);
                boolean result = enrolleeDao.checkFaculty(eid, fid);
                resultSet.put(fid, result);
            }
        } catch (ProjectException e) {
            throw e;
        } finally {
            helper.endTransaction();
        }
        return resultSet;
    }

    public static boolean checkInactive(int enrolleeId, int facultyId)
            throws ProjectException {
        boolean result = false;
        DaoHelper helper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        try {
            helper.startTransaction(enrolleeDao);
            result = enrolleeDao.checkInactive(enrolleeId, facultyId);
        } catch (ProjectException e) {
            throw e;
        } finally {
            helper.endTransaction();
        }
        return result;
    }

    public static boolean restoreFacultyRegistration(int enrolleeId, int facultyId)
            throws ProjectException {
        boolean result = false;
        DaoHelper helper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        try {
            helper.startTransaction(enrolleeDao);
            result = enrolleeDao.restoreFacultyRegistration(enrolleeId, facultyId);
        } catch (ProjectException e) {
            throw e;
        } finally {
            helper.endTransaction();
        }
        return result;
    }

    public static boolean deregisterFromFaculty(int enrolleeId, int facultyId)
            throws ProjectException {
        boolean result = false;
        DaoHelper helper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        try {
            helper.startTransaction(enrolleeDao);
            result = enrolleeDao.deregisterFromFacultyById(enrolleeId, facultyId);
        } catch (ProjectException e) {
            throw e;
        } finally {
            helper.endTransaction();
        }
        return result;
    }

}
