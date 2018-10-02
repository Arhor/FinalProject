package by.epam.admission.logic;

import by.epam.admission.dao.DaoHelper;
import by.epam.admission.dao.impl.SubjectDao;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Subject;

import java.util.List;

public class SubjectService {

    private SubjectService(){}

    public static List<Subject> findSubjects() throws ProjectException {
        DaoHelper helper = new DaoHelper();
        SubjectDao subjectDao = new SubjectDao();
        List<Subject> subjects = null;
        try {
            helper.startTransaction(subjectDao);
            subjects = subjectDao.findAll();
        } catch (ProjectException e) {
            throw e;
        } finally {
            helper.endTransaction();
        }
        return subjects;
    }

    public static Subject findSubjectById(int subjectId) throws ProjectException {
        Subject subject = null;
        DaoHelper helper = new DaoHelper();
        SubjectDao subjectDao = new SubjectDao();
        try {
            helper.startTransaction(subjectDao);
            subject = subjectDao.findEntityById(subjectId);
        } catch (ProjectException e) {
            throw e;
        } finally {
            helper.endTransaction();
        }
        return subject;
    }
}
