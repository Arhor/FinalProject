/*
 * class: DatabaseManagerTest
 */

package by.epam.admission.pool;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

/**
 * @author Burishinets Maxim
 * @version 1.0 13 Oct 2018
 */
public class DatabaseManagerTest {

    @Test
    public void testReadProperties() {
        String failMessage = "Reading database configuration file failed";
        try {
            ResourceBundle bundle = DatabaseManager.readProperties();
            Assert.assertNotNull(bundle, failMessage);
        } catch (Exception e) {
            Assert.fail(failMessage, e);
        }
    }
}