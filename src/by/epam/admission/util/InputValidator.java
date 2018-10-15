/*
 * class: InputValidator
 */

package by.epam.admission.util;

import by.epam.admission.exception.ProjectException;

/**
 * @author Burishinets Maxim
 * @version 1.0 09 Oct 2018
 * */
public class InputValidator {

    private static final String COUNTRY_CITY_PATTERN = "^[а-яА-ЯёЁa-zA-Z][-а-яА-ЯёЁa-zA-Z]{0,53}[а-яА-ЯёЁa-zA-Z]$";
    private static final String SCORE_PATTERN = "^([1-9][0-9]?)|(100)$";
    private static final String FIRST_LAST_NAME_PATTERN = "^[-а-яА-ЯёЁa-zA-Z]{2,35}$";
    private static final String EMAIL_PATTERN = "^[-a-z0-9!#$%&'*+/=?^_`{|}~]+(\\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@([a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)*([a-z]{2,4})$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,10}$";

    private static final int EMAIL_MAX_LENGTH = 129;

    private InputValidator() {}

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

}
