package by.epam.admission.service;

import by.epam.admission.dao.DaoHelper;
import by.epam.admission.dao.impl.EnrolleeDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Enrollee;
import by.epam.admission.model.Subject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TreeMap;

public class EnrolleeService {

    private static final Logger LOG =
            LogManager.getLogger(EnrolleeService.class);

    private EnrolleeService(){}

    public static boolean registerEnrollee(Enrollee enrollee)
            throws ProjectException {
        boolean result = false;
        DaoHelper daoHelper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        daoHelper.startTransaction(enrolleeDao);
        try {
            enrolleeDao.create(enrollee);
            daoHelper.commit();
            result = true;
        } catch (ProjectException e) {
            daoHelper.rollback();
            throw e;
        } finally {
            daoHelper.endTransaction();
        }
        return result;
    }

    public static Enrollee updateEnrollee(Enrollee enrollee)
            throws ProjectException {
        Enrollee result = null;
        DaoHelper daoHelper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        daoHelper.startTransaction(enrolleeDao);
        try {
            result = enrolleeDao.update(enrollee);
            daoHelper.commit();
        } catch (ProjectException e) {
            daoHelper.rollback();
            throw e;
        } finally {
            daoHelper.endTransaction();
        }
        return result;
    }

    public static Enrollee findEnrollee(int uid) throws ProjectException {
        Enrollee enrollee = null;
        DaoHelper daoHelper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        daoHelper.startTransaction(enrolleeDao);
        try {
            enrollee = enrolleeDao.findEnrolleeByUserId(uid);
            if (enrollee != null) {
                TreeMap<Subject, Integer> marks =
                        enrolleeDao.findEnrolleeMarks(enrollee.getId());
                enrollee.setMarks(marks);
            }
        } catch (ProjectException e) {
            throw e;
        } finally {
            daoHelper.endTransaction();
        }
        return enrollee;
    }

    public static boolean addSubject(int enrolleeId,
                                     int subjectId,
                                     int subjectScore) throws ProjectException {

        boolean result = false;
        DaoHelper helper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        try {
            helper.startTransaction(enrolleeDao);
            result = enrolleeDao.addSubject(enrolleeId,subjectId,subjectScore);
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
