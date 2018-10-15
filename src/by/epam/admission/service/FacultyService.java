/*
 * class: FacultyService
 */

package by.epam.admission.service;

import by.epam.admission.dao.DaoHelper;
import by.epam.admission.dao.impl.EnrolleeDao;
import by.epam.admission.dao.impl.FacultyDao;
import by.epam.admission.dao.impl.SubjectDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Faculty;
import by.epam.admission.model.Subject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * @author Burishinets Maxim
 * @version 1.0 09 Sep 2018
 */
public class FacultyService {

    private static final Logger LOG = LogManager.getLogger(FacultyService.class);

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

    public static Faculty findFaculty(int facultyId) throws ProjectException {
        DaoHelper helper = new DaoHelper();
        FacultyDao facultyDao = new FacultyDao();
        try {
            helper.startTransaction(facultyDao);
            return facultyDao.findEntityById(facultyId);
        } finally {
            helper.endTransaction();
        }
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

    public static boolean checkFacultyStatus(int facultyId) throws ProjectException {
        DaoHelper helper = new DaoHelper();
        FacultyDao facultyDao = new FacultyDao();
        try {
            helper.startTransaction(facultyDao);
            Faculty faculty = facultyDao.findEntityById(facultyId);
            LOG.debug(faculty);
            return faculty.isChecked();
        } finally {
            helper.endTransaction();
        }
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

    public static boolean checkAdmissionListEntry(int enrolleeId, int facultyId)
            throws ProjectException {
        boolean result;
        DaoHelper helper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        try {
            helper.startTransaction(enrolleeDao);
            result = enrolleeDao.checkAdmissionListStatus(enrolleeId, facultyId);
        } finally {
            helper.endTransaction();
        }
        return result;
    }

    public static boolean checkAvailability() throws ProjectException {
        boolean result;
        DaoHelper helper = new DaoHelper();
        FacultyDao facultyDao = new FacultyDao();
        try {
            helper.startTransaction(facultyDao);
            result = facultyDao.checkFaculties();
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

    public static boolean defineFacultyResult(int facultyId) throws ProjectException {
        boolean result = true;
        DaoHelper helper = new DaoHelper();
        FacultyDao facultyDao = new FacultyDao();
        EnrolleeDao enrolleeDao = new EnrolleeDao();

        try {
            helper.startTransaction(facultyDao, enrolleeDao);

            Faculty faculty = facultyDao.findEntityById(facultyId);

            ArrayList<Integer> idsForBudget = enrolleeDao.findBestEnrolleesIds(facultyId,0, faculty.getSeatsBudget());
            ArrayList<Integer> idsForPaid = enrolleeDao.findBestEnrolleesIds(facultyId, faculty.getSeatsBudget(), faculty.getSeatsPaid());

            if (!idsForBudget.isEmpty()) {
                boolean resultBudget = enrolleeDao.updateAdmissionList(facultyId, idsForBudget, EnrolleeDao.Seats.BUDGET);
                result = (result && resultBudget);
            }
            if (!idsForPaid.isEmpty()) {
                boolean resultPaid = enrolleeDao.updateAdmissionList(facultyId, idsForPaid, EnrolleeDao.Seats.PAID);
                result = (result && resultPaid);
            }

            faculty.setChecked(true);

            boolean resultCheck = facultyDao.update(faculty);

            result = (result && resultCheck);

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
