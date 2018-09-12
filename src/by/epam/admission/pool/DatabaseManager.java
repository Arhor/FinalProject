/*
 * class: DatabaseManager
 */

package by.epam.admission.pool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Maxim Burishinets
 * @version 1.0 25 Aug 2018
 */
class DatabaseManager {
    
    private static final Logger LOG = LogManager.getLogger();
    private static final String PROPERTY_PATH = "resources.database";

    private DatabaseManager(){}

    static ResourceBundle readProperties() {
        return ResourceBundle.getBundle(PROPERTY_PATH);
    }
}
