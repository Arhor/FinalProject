/*
 * class: ConfigurationManager
 */

package by.epam.admission.util;

import java.util.ResourceBundle;

/**
 * Class ConfigurationManager serves as container for jsp URI
 *
 * @author Burishinets Maxim
 * @version 1.0 20 Aug 2018
 */
public class ConfigurationManager {

    private static final ResourceBundle RESOURCE_BUNDLE
            = ResourceBundle.getBundle("resources.config");

    private ConfigurationManager(){}

    /**
     * @param key property key for concrete jsp page
     * @return jsp URI
     */
    public static String getProperty(String key) {
        return RESOURCE_BUNDLE.getString(key);
    }

}
