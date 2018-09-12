/*
 * class: User
 */

package by.epam.admission.model;

/**
 * @author Maxim Burishinets
 * @version 1.0 29 Aug 2018
 */
public class User extends Entity {

    private static final long serialVersionUID = -8340115208595108089L;
    
    private String firstName;
    private String lastName;
    private String email;
    private Lang lang;
    private Role role;

    public User() {
        super();
    }

    public User(int id) {
        super(id);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public enum Lang {
        RU, EN;
    }

    public enum Role {
        GUEST, CLIENT, ADMIN
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) { return true; }
        if (obj == null) { return false; }
        if (obj.getClass() != getClass()) { return false; }
        User user = (User) obj;
        if (role != user.role) { return false; }
        if (lang != user.lang) { return false; }
        if (firstName == null) {
            if (user.firstName != null) {
                return false;
            }
        } else if (!firstName.equals(user.firstName)) {
            return false;
        }
        if (lastName == null) {
            if (user.lastName != null) {
                return false;
            }
        } else if (!lastName.equals(user.lastName)) {
            return false;
        }
        if (email == null) {
            if (user.email != null) {
                return false;
            }
        } else if (!email.equals(user.email)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        hashCode += firstName == null ? 0 : firstName.hashCode();
        hashCode += lastName == null ? 0 : lastName.hashCode();
        hashCode += email == null ? 0 : email.hashCode();
        hashCode += role == null ? 0 : role.hashCode();
        hashCode += lang == null ? 0 : lang.hashCode();
        return hashCode;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "[ID=" + getId()
                + ", email=" + email
                + ", first_name=" + firstName
                + ", last_name=" + lastName
                + ", lang=" + lang
                + ", role=" + role + "]";
    }

}
