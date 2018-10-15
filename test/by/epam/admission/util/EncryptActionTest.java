package by.epam.admission.util;

import by.epam.admission.exception.ProjectException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class EncryptActionTest {

    @Test(dataProvider = "strings to encrypt", description = "positive test")
    public void testEncryptPositive(String toEncrypt, String salt)
            throws ProjectException {
        String encrypted = EncryptAction.encrypt(toEncrypt, salt);
        int expected = 128;
        int actual = encrypted.length();
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "invalid params", description = "negative test",
            expectedExceptions = {ProjectException.class})
    public void testEncryptNegative(String toEncrypt, String salt)
            throws ProjectException {
        EncryptAction.encrypt(toEncrypt, salt);
    }

    @DataProvider(name = "strings to encrypt")
    public Object[][] createData() {
        return new Object[][] {
                {"", ""},
                {"a", "a"},
                {"a", ""},
                {"", "a"},
                {" ", " "},
                {"a", "b"},
                {"b", "a"},
                {"test", "string"},
                {"string", "test"}
        };
    }

    @DataProvider(name = "invalid params")
    public Object[][] createInvalidData() {
        return new Object[][] {
                {null, ""},
                {"", null},
                {null, null}
        };
    }

}