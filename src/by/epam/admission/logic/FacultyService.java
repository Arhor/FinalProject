package by.epam.admission.logic;

import by.epam.admission.dao.DaoHelper;
import by.epam.admission.dao.impl.EnrolleeDao;
import by.epam.admission.dao.impl.FacultyDao;
import by.epam.admission.dao.impl.SubjectDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Enrollee;
import by.epam.admission.model.Faculty;
import by.epam.admission.model.Subject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class FacultyService {

    private static final Logger LOG = LogManager.getLogger(FacultyService.class);

    public static List<Faculty> findFaculties() throws ProjectException {
        List<Faculty> faculties = null;
        DaoHelper helper = new DaoHelper();
        FacultyDao facultyDao = new FacultyDao();
        SubjectDao subjectDao = new SubjectDao();
        try {
            helper.startTransaction(facultyDao, subjectDao);
            faculties = facultyDao.findAll();
            for (Faculty faculty : faculties) {
                TreeSet<Subject> subjects =
                        subjectDao.findSubjectsByFacultyId(faculty.getId());
                faculty.setSubjects(subjects);
            }
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

    public static HashMap<Integer, Boolean> checkFaculty(
            int enrolleeId, Set<Subject> enrolleeSubjects, String[] facultyIds)
            throws ProjectException {
        HashMap<Integer, Boolean> resultSet = null;
        DaoHelper helper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        SubjectDao subjectDao = new SubjectDao();
        try {
            helper.startTransaction(enrolleeDao, subjectDao);
            resultSet = new HashMap<>();
            for (String facultyId : facultyIds) {
                int fid = Integer.parseInt(facultyId);
                Set<Subject> facultySubjects =
                        subjectDao.findSubjectsByFacultyId(fid);
                if (enrolleeSubjects.containsAll(facultySubjects)) {
                    boolean result = enrolleeDao.checkFaculty(enrolleeId, fid);
                    resultSet.put(fid, result);
                }
            }
            LOG.debug(resultSet);
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
