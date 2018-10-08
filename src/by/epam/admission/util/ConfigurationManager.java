/*
 * class: ConfigurationManager
 */

package by.epam.admission.util;

import java.util.ResourceBundle;

/**
 * @author Burishinets Maxim
 * @version 1.0 20 Aug 2018
 */
public class ConfigurationManager {

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.config");

    private ConfigurationManager(){}

    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }

}
