package by.epam.admission.util;

public class EmailValidator {

    public static final String EMAIL_PATTERN =
            "[-a-z0-9!#$%&'*+/=?^_`{|}~]+(\\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@" +
            "([a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)*([a-z]{2,4})";

    private EmailValidator(){}

    public static boolean validate(String email) {
        boolean result = false;
        if (email != null) {
            result = email.matches(EMAIL_PATTERN);
        }
        return result;
    }
}
