package by.epam.admission.model;

import java.util.HashMap;

public class Enrollee extends Entity{

    private String country;
    private String city;
    private int schoolCertificate;
    private int userId;
    private boolean available;
    private HashMap<Subject, Integer> marks;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getSchoolCertificate() {
        return schoolCertificate;
    }

    public void setSchoolCertificate(int schoolCertificate) {
        this.schoolCertificate = schoolCertificate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public HashMap<Subject, Integer> getMarks() {
        return marks;
    }

    public void setMarks(HashMap<Subject, Integer> marks) {
        this.marks = marks;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) { return true; }
        if (obj == null) { return false; }
        if (obj.getClass() != getClass()) { return false; }
        Enrollee enrollee = (Enrollee) obj;
        if (schoolCertificate != enrollee.schoolCertificate) { return false; }
        if (userId != enrollee.getUserId()) { return false; }
        if (country == null) {
            if (enrollee.country != null) {
                return false;
            }
        } else if (!country.equals(enrollee.country)) {
            return false;
        }
        if (city == null) {
            if (enrollee.city != null) {
                return false;
            }
        } else if (!city.equals(enrollee.city)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        hashCode += country == null ? 0 : country.hashCode();
        hashCode += city == null ? 0 : city.hashCode();
        hashCode += 31 * schoolCertificate;
        hashCode += 31 * userId;
        return hashCode;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + " [ID=" + getId()
                + ", country=" + country
                + ", city=" + city
                + ", school_certificate=" + schoolCertificate
                + ", UID=" + userId + "]";
    }

}