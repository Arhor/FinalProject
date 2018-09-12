package by.epam.admission.util;

import java.util.ResourceBundle;

public class MessageManager {

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.messages");

    private MessageManager(){}

    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }

}
