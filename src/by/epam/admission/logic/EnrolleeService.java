package by.epam.admission.logic;

import by.epam.admission.dao.TransactionHelper;
import by.epam.admission.dao.impl.EnrolleeDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Enrollee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnrolleeService {

    private static final Logger LOG = LogManager.getLogger(EnrolleeService.class);

    public static boolean registerEnrollee(Enrollee enrollee) {
        boolean result = false;
        TransactionHelper transactionHelper = new TransactionHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        transactionHelper.startTransaction(enrolleeDao);
        try {
            enrolleeDao.create(enrollee);
            transactionHelper.commit();
            result = true;
        } catch (ProjectException e) {
            transactionHelper.rollback();
            LOG.debug(e);
        } finally {
            transactionHelper.endTransaction();
        }
        return result;
    }

    public static Enrollee updateEnrollee(Enrollee enrollee) {
        Enrollee result;
        TransactionHelper transactionHelper = new TransactionHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        transactionHelper.startTransaction(enrolleeDao);
        try {
            result = enrolleeDao.update(enrollee);
            transactionHelper.commit();
        } catch (ProjectException e) {
            result = enrollee;
            transactionHelper.rollback();
        } finally {
            transactionHelper.endTransaction();
        }
        return result;
    }

    public static Enrollee findEnrollee(int uid) {
        Enrollee enrollee = null;

        TransactionHelper transactionHelper = new TransactionHelper();
        EnrolleeDao enrolleeDao = new EnrolleeDao();
        transactionHelper.startTransaction(enrolleeDao);
        try {
            enrollee = enrolleeDao.findEnrolleeByUID(uid);
        } catch (ProjectException e) {
            LOG.error(e);
        } finally {
            transactionHelper.endTransaction();
        }

        return enrollee;
    }
}
