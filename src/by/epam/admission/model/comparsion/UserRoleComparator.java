package by.epam.admission.model.comparsion;

import by.epam.admission.model.User;

import java.util.Comparator;

public class UserRoleComparator implements Comparator<User> {

    @Override
    public int compare(User userOne, User userTwo) {
        return userTwo.getRole().compareTo(userOne.getRole());
    }
}
