package by.epam.admission.util;

public class ConfirmationCodeGenerator {

    private static final int CODE_OFFSET = 65531;
    private static final int CODE_VARIATION = 1048575;

    private ConfirmationCodeGenerator(){}

    public static String generate() {
        int code = (int) (Math.random() * CODE_VARIATION + CODE_OFFSET);
        return Integer.toHexString(code).toUpperCase();
    }
}
