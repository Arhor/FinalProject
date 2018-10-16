/*
 * class: MessageManager
 */

package by.epam.admission.util;

import java.util.Locale;
import java.util.ResourceBundle;

import static by.epam.admission.model.User.*;

/**
 * Class MessageManager used to receive messages from bundle depending on passed
 * {@link Lang} object
 *
 * @author Burishinets Maxim
 * @version 1.0 20 Aug 2018
 */
public class MessageManager {

    private static final ResourceBundle RESOURCE_BUNDLE_EN;
    private static final ResourceBundle RESOURCE_BUNDLE_RU;

    private MessageManager(){}

    /**
     * Method takes String as key and {@link Lang} object to define bundle to
     * retrieve message value from
     *
     * @param key String property key for concrete message
     * @param lang current session's language
     * @return text message
     */
    public static String getProperty(String key, Lang lang) {
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

    static {
        RESOURCE_BUNDLE_EN = ResourceBundle.getBundle(
                "resources.message", new Locale("en", "US"));
        RESOURCE_BUNDLE_RU = ResourceBundle.getBundle(
                "resources.message", new Locale("ru", "RU"));
    }

}
