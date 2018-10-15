/*
 * class: DatabaseManager
 */

package by.epam.admission.pool;

import java.util.ResourceBundle;

/**
 * Class DatabaseManager used to read database configuration file
 *
 * @author Maxim Burishinets
 * @version 1.0 25 Aug 2018
 */
class DatabaseManager {
    
    private static final String PROPERTY_PATH = "resources.database";

    private DatabaseManager(){}

    static ResourceBundle readProperties() {
        return ResourceBundle.getBundle(PROPERTY_PATH);
    }
}
