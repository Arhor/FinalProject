/*
 * class: Subject
 */

package by.epam.admission.model;

/**
 * Class Subject represents concrete subject entity
 *
 * @author Maxim Burishinets
 * @version 1.0 29 Aug 2018
 */
public class Subject extends Entity {

    private static final long serialVersionUID = -6765114330753627475L;

    private String nameRu;
    private String nameEn;

    public Subject() {
        super();
    }

    public Subject(int id, String nameRu, String nameEn) {
        super(id);
        this.nameRu = nameRu;
        this.nameEn = nameEn;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) { return true; }
        if (obj == null) { return false; }
        if (obj.getClass() != getClass()) { return false; }
        Subject subject = (Subject) obj;
        if (subject.getId() != this.getId()) { return false; }
        if (nameRu == null) {
            if (subject.nameRu != null) {
                return false;
            }
        } else if (!nameRu.equals(subject.nameRu)) {
            return false;
        }
        if (nameEn == null) {
            if (subject.nameEn != null) {
                return false;
            }
        } else if (!nameEn.equals(subject.nameEn)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        hashCode += nameRu == null ? 0 : nameRu.hashCode();
        hashCode += nameEn == null ? 0 : nameEn.hashCode();
        return hashCode;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "[ID=" + getId()
                + ", name_ru=" + nameRu
                + ", name_en=" + nameEn + "]";
    }
}
