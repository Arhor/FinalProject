/*
 * class: Enrollee
 */

package by.epam.admission.model;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Burishinets Maxim
 * @version 1.0 29 Aug 2018
 */
public class Enrollee extends Entity{

    private String country;
    private String city;
    private int schoolCertificate;
    private int userId;
    private TreeMap<Subject, Integer> marks;

    public Enrollee() {
        super();
    }

    public Enrollee(String country,
                    String city,
                    int schoolCertificate,
                    int userId) {
        this.country = country;
        this.city = city;
        this.schoolCertificate = schoolCertificate;
        this.userId = userId;
    }

    public Enrollee(int id,
                    String country,
                    String city,
                    int schoolCertificate,
                    int userId) {
        super(id);
        this.country = country;
        this.city = city;
        this.schoolCertificate = schoolCertificate;
        this.userId = userId;
    }

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

    public TreeMap<Subject, Integer> getMarks() {
        return marks;
    }

    public void setMarks(TreeMap<Subject, Integer> marks) {
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
        if (marks == null) {
            if (enrollee.marks != null) {
                return false;
            }
        } else if (!marks.equals(enrollee.getMarks())) {
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
        for (Map.Entry<Subject, Integer> mark : marks.entrySet()) {
            Subject subject = mark.getKey();
            Integer score = mark.getValue();
            hashCode += subject == null ? 0 : subject.hashCode();
            hashCode += score == null ? 0 : score.hashCode();
        }
        return hashCode;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + " [ID=" + getId()
                + ", country=" + country
                + ", city=" + city
                + ", school_certificate=" + schoolCertificate
                + ", UID=" + userId
                + ", marks=" + marks + "]";
    }

}