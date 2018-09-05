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
        ADMIN, CLIENT;
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
