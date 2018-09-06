package by.epam.admission.model.comparsion;

import by.epam.admission.model.Entity;

import java.util.Comparator;

public class IdComparator implements Comparator<Entity> {

    @Override
    public int compare(Entity entityOne, Entity entityTwo) {
        return entityTwo.getId() - entityOne.getId();
    }
}
