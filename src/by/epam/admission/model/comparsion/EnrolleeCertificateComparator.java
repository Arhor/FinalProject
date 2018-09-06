package by.epam.admission.model.comparsion;

import by.epam.admission.model.Enrollee;

import java.util.Comparator;

public class EnrolleeCertificateComparator implements Comparator<Enrollee> {

    @Override
    public int compare(Enrollee enrolleeOne, Enrollee enrolleeTwo) {
        int firstCertificate = enrolleeOne.getSchoolCertificate();
        int secondCertificate = enrolleeTwo.getSchoolCertificate();
        return secondCertificate - firstCertificate;
    }
}
