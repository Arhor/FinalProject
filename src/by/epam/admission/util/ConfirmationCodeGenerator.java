/*
 * class: ConfirmationCodeGenerator
 */

package by.epam.admission.util;

/**
 * Class ConfirmationCodeGenerator used to create codes of fixed length
 *
 * @author Burishinets Maxim
 * @version 1.0 20 Aug 2018
 */
public class ConfirmationCodeGenerator {

    // codes generated between '100000' and 'FFFFFF'
    private static final int CODE_OFFSET = 1048576;
    private static final int CODE_VARIATION = 15728639;

    private ConfirmationCodeGenerator(){}

    /**
     * Method generates random values ranging from '100000' to 'FFFFFF' and
     * returns its string representation
     *
     * @return six-digit code ranging from '100000' to 'FFFFFF'
     */
    public static String generate() {
        int code = (int) (Math.random() * CODE_VARIATION + CODE_OFFSET);
        return Integer.toHexString(code).toUpperCase();
    }
}
