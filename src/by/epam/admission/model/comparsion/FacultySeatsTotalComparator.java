package by.epam.admission.model.comparsion;

import by.epam.admission.model.Faculty;

import java.util.Comparator;

public class FacultySeatsTotalComparator implements Comparator<Faculty> {

    @Override
    public int compare(Faculty facultyOne, Faculty facultyTwo) {
        int totalOne = facultyOne.getSeatsBudget() + facultyOne.getSeatsPaid();
        int totalTwo = facultyTwo.getSeatsBudget() + facultyTwo.getSeatsPaid();
        return totalTwo - totalOne;
    }
}
