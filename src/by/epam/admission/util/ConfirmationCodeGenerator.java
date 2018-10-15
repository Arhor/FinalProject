/*
 * class: ConfirmationCodeGenerator
 */

package by.epam.admission.util;

/**
 * @author Burishinets Maxim
 * @version 1.0 20 Aug 2018
 */
public class ConfirmationCodeGenerator {

    // codes geberated between 100000 and FFFFFF
    private static final int CODE_OFFSET = 1048576;
    private static final int CODE_VARIATION = 15728639;

    private ConfirmationCodeGenerator(){}

    public static String generate() {
        int code = (int) (Math.random() * CODE_VARIATION + CODE_OFFSET);
        return Integer.toHexString(code).toUpperCase();
    }
}
