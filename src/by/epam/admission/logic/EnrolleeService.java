package by.epam.admission.logic;

import by.epam.admission.dao.DaoHelper;
import by.epam.admission.dao.impl.EnrolleeDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Enrollee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TreeMap;

public class EnrolleeService {

    private static final Logger LOG = LogManager.getLogger(EnrolleeService.class);

    public static boolean registerEnrollee(Enrollee enrollee) {
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
            LOG.debug(e);
        } finally {
            daoHelper.endTransaction();
        }
        return result;
    }

    public static Enrollee updateEnrollee(Enrollee enrollee) {
        Enrollee result;
        DaoHelper daoHelper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        daoHelper.startTransaction(enrolleeDao);
        try {
            result = enrolleeDao.update(enrollee);
            daoHelper.commit();
        } catch (ProjectException e) {
            result = enrollee;
            daoHelper.rollback();
        } finally {
            daoHelper.endTransaction();
        }
        return result;
    }

    public static Enrollee findEnrollee(int uid) {
        Enrollee enrollee = null;

        DaoHelper daoHelper = new DaoHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        daoHelper.startTransaction(enrolleeDao);
        try {
            enrollee = enrolleeDao.findEnrolleeByUID(uid);
            TreeMap<Integer, Integer> marks =
                    enrolleeDao.findEnrolleeMarks(enrollee.getUserId());
            enrollee.setMarks(marks);
        } catch (ProjectException e) {
            LOG.error(e);
        } finally {
            daoHelper.endTransaction();
        }

        return enrollee;
    }
}
