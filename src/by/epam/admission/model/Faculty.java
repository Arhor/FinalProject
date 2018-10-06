/*
 * class: Faculty
 */

package by.epam.admission.model;

import java.util.TreeSet;

/**
 * @author Maxim Burishinets
 * @version 1.0 29 Aug 2018
 */
public class Faculty extends Entity {

    private static final long serialVersionUID = 5662696934735451425L;
    
    private String nameRu;
    private String nameEn;
    private int seatsPaid;
    private int seatsBudget;
    private TreeSet<Subject> subjects;
    private boolean checked;

    public Faculty() {
        super();
    }

    public Faculty(int id, String nameRu, String nameEn, int seatsPaid,
                   int seatsBudget) {
        super(id);
        this.nameRu = nameRu;
        this.nameEn = nameEn;
        this.seatsPaid = seatsPaid;
        this.seatsBudget = seatsBudget;
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

    public int getSeatsPaid() {
        return seatsPaid;
    }

    public void setSeatsPaid(int seatsPaid) {
        this.seatsPaid = seatsPaid;
    }

    public int getSeatsBudget() {
        return seatsBudget;
    }

    public void setSeatsBudget(int seatsBudget) {
        this.seatsBudget = seatsBudget;
    }

    public TreeSet<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(TreeSet<Subject> subjects) {
        this.subjects = subjects;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) { return true; }
        if (obj == null) { return false; }
        if (obj.getClass() != getClass()) { return false; }
        Faculty faculty = (Faculty) obj;
        if (seatsPaid != faculty.seatsPaid) { return false; }
        if (seatsBudget != faculty.seatsBudget) { return false; }
        if (nameRu == null) {
            if (faculty.nameRu != null) {
                return false;
            }
        } else if (!nameRu.equals(faculty.nameRu)) {
            return false;
        }
        if (nameEn == null) {
            if (faculty.nameEn != null) {
                return false;
            }
        } else if (!nameEn.equals(faculty.nameEn)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        hashCode += nameRu == null ? 0 : nameRu.hashCode();
        hashCode += nameEn == null ? 0 : nameEn.hashCode();
        hashCode += 31 * seatsPaid;
        hashCode += 31 * seatsBudget;
        return hashCode;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "[ID=" + getId()
                + ", name_ru='" + nameRu
                + "', name_en='" + nameEn
                + "', seats_paid=" + seatsPaid
                + ", seats_budget=" + seatsBudget + "]";
    }

}
