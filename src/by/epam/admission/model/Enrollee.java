package by.epam.admission.model;

public class Enrollee extends Entity{

    private String country;
    private String city;
    private int schoolCertificate;
    private int userId;

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