/*
 * class: InputValidator
 */

package by.epam.admission.util;

import by.epam.admission.exception.ProjectException;

/**
 * Class InputValidator used to validate various types of user submitted
 * information
 *
 * @author Burishinets Maxim
 * @version 1.0 09 Oct 2018
 * */
public class InputValidator {

    private static final String COUNTRY_CITY_PATTERN;
    private static final String SCORE_PATTERN;
    private static final String FIRST_LAST_NAME_PATTERN;
    private static final String EMAIL_PATTERN;
    private static final String PASSWORD_PATTERN;

    private static final int EMAIL_MAX_LENGTH = 129;

    private InputValidator() {}

    /**
     * Method validates user's input depending on passed
     * {@link by.epam.admission.command.Router.Type} object
     *
     * @param input submitted by user text line
     * @param type {@link by.epam.admission.command.Router.Type} object that
     *             represents user's input type
     * @return 'true' in case of valid user's input, otherwise 'false'
     * @throws ProjectException in case of undefined passed Type object
     */
    public static boolean validate(String input, InputType type)
            throws ProjectException {
        boolean result = false;
        String currentPattern;
        if (input != null && type != null) {
            switch (type) {
                case CITY:
                case COUNTRY:
                    currentPattern = COUNTRY_CITY_PATTERN;
                    break;
                case CERTIFICATE:
                case SCORE:
                    currentPattern = SCORE_PATTERN;
                    break;
                case FIRST_NAME:
                case LAST_NAME:
                    currentPattern = FIRST_LAST_NAME_PATTERN;
                    break;
                case EMAIL:
                    if (input.length() > EMAIL_MAX_LENGTH) {
                        return false;
                    }
                    currentPattern = EMAIL_PATTERN;
                    break;
                case PASSWORD:
                    currentPattern = PASSWORD_PATTERN;
                    break;
                default:
                    throw new ProjectException("Incorrect input type");
            }
            result = input.matches(currentPattern);
        }
        return result;
    }

    public enum InputType {
        CITY, COUNTRY, CERTIFICATE, SCORE, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD
    }

    static {
        COUNTRY_CITY_PATTERN =
                "^" +
                "[а-яА-ЯёЁa-zA-Z]" +
                "[-а-яА-ЯёЁa-zA-Z]{0,53}" +
                "[а-яА-ЯёЁa-zA-Z]" +
                "$";
        SCORE_PATTERN =
                "^" +
                "([1-9][0-9]?)|(100)" +
                "$";
        FIRST_LAST_NAME_PATTERN =
                "^" +
                "[-а-яА-ЯёЁa-zA-Z]{2,35}" +
                "$";
        EMAIL_PATTERN =
                "^" +
                "[-a-z0-9!#$%&'*+/=?^_`{|}~]+" +
                "(\\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*" +
                "@" +
                "([a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)*" +
                "([a-z]{2,4})" +
                "$";
        PASSWORD_PATTERN =
                "^" +
                "(?=.*[0-9])" +
                "(?=.*[a-z])" +
                "(?=.*[A-Z])" +
                "(?=\\S+$)" +
                ".{6,10}" +
                "$";
    }

}
