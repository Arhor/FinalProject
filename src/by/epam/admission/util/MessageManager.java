/*
 * class: MessageManager
 */

package by.epam.admission.util;

import by.epam.admission.model.User;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Burishinets Maxim
 * @version 1.0 20 Aug 2018
 */
public class MessageManager {

    private static final ResourceBundle RESOURCE_BUNDLE_EN = ResourceBundle.getBundle("resources.message", new Locale("en", "US"));
    private static final ResourceBundle RESOURCE_BUNDLE_RU = ResourceBundle.getBundle("resources.message", new Locale("ru", "RU"));

    private MessageManager(){}

    public static String getProperty(String key, User.Lang lang) {
        String message;
        switch (lang) {
            case RU:
                message = RESOURCE_BUNDLE_RU.getString(key);
                break;
            case EN:
            default:
                message = RESOURCE_BUNDLE_EN.getString(key);
                break;
        }
        return message;
    }

}
