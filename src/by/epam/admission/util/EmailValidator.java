/*
 * class: EmailValidator
 */

package by.epam.admission.util;

/**
 * @author Burishinets Maxim
 * @version 1.0 20 Aug 2018
 */
public class EmailValidator {

    private static final String EMAIL_PATTERN =
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
