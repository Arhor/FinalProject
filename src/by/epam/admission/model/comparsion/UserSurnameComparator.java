package by.epam.admission.model.comparsion;

import by.epam.admission.model.User;

import java.util.Comparator;

public class UserSurnameComparator implements Comparator<User> {

    @Override
    public int compare(User userOne, User userTwo) {
        return userTwo.getLastName().compareTo(userOne.getLastName());
    }
}
