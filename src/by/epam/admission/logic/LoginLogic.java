package by.epam.admission.logic;

public class LoginLogic {

    private static final String ADMIN_LOGIN = "admin";
    private static final String ADMIN_PASSWORD = "qwe123";

    public static boolean checkLogin(String login, String password) {
        return ADMIN_LOGIN.equals(login) && ADMIN_PASSWORD.equals(password);
    }

}
