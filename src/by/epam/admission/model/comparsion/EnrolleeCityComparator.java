package by.epam.admission.model.comparsion;

import by.epam.admission.model.Enrollee;

import java.util.Comparator;

public class EnrolleeCityComparator implements Comparator<Enrollee> {

    @Override
    public int compare(Enrollee enrolleeOne, Enrollee enrolleeTwo) {
        return enrolleeTwo.getCity().compareTo(enrolleeOne.getCity());
    }
}
