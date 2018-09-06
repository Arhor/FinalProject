package by.epam.admission.model.comparsion;

import by.epam.admission.model.Faculty;

import java.util.Comparator;

public class FacultySeatsPaidComparator implements Comparator<Faculty> {

    @Override
    public int compare(Faculty facultyOne, Faculty facultyTwo) {
        return facultyTwo.getSeatsPaid() - facultyOne.getSeatsPaid();
    }
}
