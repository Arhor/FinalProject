/*
 * class: FacultyService
 */

package by.epam.admission.logic;

import by.epam.admission.dao.DaoHelper;
import by.epam.admission.dao.impl.EnrolleeDao;
import by.epam.admission.dao.impl.FacultyDao;
import by.epam.admission.dao.impl.SubjectDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Faculty;
import by.epam.admission.model.Subject;

import java.util.*;

/**
 * @author Burishinets Maxim
 * @version 1.0 09 Sep 2018
 */
public class FacultyService {

    public static List<Faculty> findFaculties() throws ProjectException {
        List<Faculty> faculties;
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
        } finally {
            helper.endTransaction();
        }
        return faculties;
    }

    public static boolean registerToFaculty(int enrolleeId, int facultyId)
            throws ProjectException {
        boolean result;
        DaoHelper helper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        try {
            helper.startTransaction(enrolleeDao);
            result = enrolleeDao.registerToFacultyById(enrolleeId, facultyId);
            helper.commit();
        } catch (ProjectException e) {
            helper.rollback();
            throw e;
        } finally {
            helper.endTransaction();
        }
        return result;
    }

    public static HashMap<Integer, Boolean> checkFaculties(
            int enrolleeId, Set<Subject> enrolleeSubjects, String[] facultyIds)
            throws ProjectException {
        HashMap<Integer, Boolean> resultSet;
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
        } finally {
            helper.endTransaction();
        }
        return resultSet;
    }

    public static boolean checkFaculty(int enrolleeId, int facultyId)
            throws ProjectException {
        boolean result;
        DaoHelper helper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        try {
            helper.startTransaction(enrolleeDao);
            result = enrolleeDao.checkFaculty(enrolleeId, facultyId);
        } finally {
            helper.endTransaction();
        }
        return result;
    }

    public static boolean checkAdmissionListEntry(int enrolleeId, int facultyId)
            throws ProjectException {
        boolean result;
        DaoHelper helper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        try {
            helper.startTransaction(enrolleeDao);
            result = enrolleeDao.checkAdmissionListEntry(enrolleeId, facultyId);
        } finally {
            helper.endTransaction();
        }
        return result;
    }

    public static boolean restoreFacultyRegistration(int enrolleeId,
                                                     int facultyId)
            throws ProjectException {
        boolean result;
        DaoHelper helper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        try {
            helper.startTransaction(enrolleeDao);
            result = enrolleeDao.restoreFacultyRegistration(
                    enrolleeId, facultyId);
            helper.commit();
        } catch (ProjectException e) {
            helper.rollback();
            throw e;
        } finally {
            helper.endTransaction();
        }
        return result;
    }

    public static boolean deregisterFromFaculty(int enrolleeId, int facultyId)
            throws ProjectException {
        boolean result;
        DaoHelper helper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        try {
            helper.startTransaction(enrolleeDao);
            result = enrolleeDao.deregisterFromFacultyById(
                    enrolleeId, facultyId);
            helper.commit();
        } catch (ProjectException e) {
            helper.rollback();
            throw e;
        } finally {
            helper.endTransaction();
        }
        return result;
    }

}
