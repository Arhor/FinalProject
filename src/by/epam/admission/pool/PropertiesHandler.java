/*
 * class: PropertiesHandler
 */

package by.epam.admission.pool;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Maxim Burishinets
 * @version 1.0 25 Aug 2018
 */
class PropertiesHandler {
    
    private static final Logger LOG = LogManager.getLogger();
    private static final String PROPERTY_PATH = "resources/database.properties";

    private PropertiesHandler(){}

    static Properties readProperties() {
        try (FileInputStream fis = new FileInputStream(PROPERTY_PATH)) {
            Properties prop = new Properties();
            prop.load(fis);
            return prop;
        } catch (IOException e) {
            LOG.fatal("I/O exception", e);
            throw new RuntimeException();
        }
    }
}
