/*
 * class: Entity
 */

package by.epam.admission.model;

import java.io.Serializable;

/**
 * @author Burishinets Maxim
 * @version 1.0 29 Aug 2018
 */
public abstract class Entity implements Serializable, Cloneable, Comparable<Entity> {

    private static final long serialVersionUID = -1825429275979947811L;
    
    private int id;

    public Entity() {
        super();
    }

    public Entity(int id) {
        this.setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(Entity entity) {
        return entity.id - this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) { return true; }
        if (obj == null) { return false; }
        if (obj.getClass() != getClass()) { return false; }
        Entity entity = (Entity) obj;
        if (id != entity.id) { return false; }
        return true;
    }

    @Override
    public int hashCode() {
        return 31 * id;
    }

}
