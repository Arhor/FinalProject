/*
 * class: Faculty
 */

package by.epam.admission.model;

/**
 * @author Maxim Burishinets
 * @version 1.0 29 Aug 2018
 */
public class Faculty extends Entity {

    private static final long serialVersionUID = 5662696934735451425L;
    
    private String nameRu;
    private String nameEn;
    private int seatsTotal;
    private int seatsBudget;

    public Faculty() {
        super();
    }

    public Faculty(int id, String nameRu, String nameEn, int seatsTotal, int seatsBudget) {
        super(id);
        this.nameRu = nameRu;
        this.nameEn = nameEn;
        this.seatsTotal = seatsTotal;
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

    public int getSeatsTotal() {
        return seatsTotal;
    }

    public void setSeatsTotal(int seatsTotal) {
        this.seatsTotal = seatsTotal;
    }

    public int getSeatsBudget() {
        return seatsBudget;
    }

    public void setSeatsBudget(int seatsBudget) {
        this.seatsBudget = seatsBudget;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "[ID=" + getId()
                + ", name_ru='" + nameRu
                + "', name_en='" + nameEn
                + "', seats_total=" + seatsTotal
                + ", seats_budget=" + seatsBudget + "]";
    }

}
