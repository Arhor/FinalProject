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
    public boolean equals(Object obj) {
        if (!super.equals(obj)) { return false; }
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