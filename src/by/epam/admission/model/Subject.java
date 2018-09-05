/*
 * class: Subject
 */

package by.epam.admission.model;

/**
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
    public String toString() {
        return getClass().getSimpleName()
                + "ID=" + getId()
                + ", name_ru=" + nameRu
                + ", name_en=" + nameEn + "]";
    }
}
