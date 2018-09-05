package by.epam.admission.model;

import java.util.HashSet;

public class Enrollee extends Entity{

    private String country;
    private String city;
    private int schoolCertificate;
    private boolean passed;
    private int userId;
    private int facultyId;

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

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + " [ID=" + getId()
                + ", country=" + country
                + ", city=" + city
                + ", school_certificate=" + schoolCertificate
                + ", passed=" + passed
                + ", UID=" + userId
                + ", faculty_ID=" + facultyId + "]";
    }

}