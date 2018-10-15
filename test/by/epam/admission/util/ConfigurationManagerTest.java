package by.epam.admission.util;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.MissingResourceException;

public class ConfigurationManagerTest {

    @Test(dataProvider = "valid keys", description = "positive test")
    public void testGetPropertyPositive(String key) {
        String property = ConfigurationManager.getProperty(key);
        Assert.assertNotNull(property);
    }

    @Test(dataProvider = "invalid keys", description = "negative test",
            expectedExceptions = {MissingResourceException.class})
    public void testGetPropertyNegative(String key) {
        ConfigurationManager.getProperty(key);
    }

    @DataProvider(name = "valid keys")
    public Object[][] createValidData() {
        return new Object[][] {
                {"path.page.index"},
                {"path.page.main"},
                {"path.page.faculties"},
                {"path.page.login"},
                {"path.page.registration"},
                {"path.page.confirm"},
                {"path.page.error"},
                {"path.page.client.main"},
                {"path.page.admin.main"}
        };
    }

    @DataProvider(name = "invalid keys")
    public Object[][] createInvalidData() {
        return new Object[][] {
                {"page.index"},
                {"page.main"},
                {"page.faculties"},
                {"page.login"},
                {"page.registration"},
                {"page.confirm"},
                {"page.error"},
                {"page.client.main"},
                {"page.admin.main"}
        };
    }

}