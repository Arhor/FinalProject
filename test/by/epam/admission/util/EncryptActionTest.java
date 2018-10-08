package by.epam.admission.util;

import by.epam.admission.exception.ProjectException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class EncryptActionTest {

    private static final Logger LOG = LogManager.getLogger(EncryptActionTest.class);

    @Test
    public void testEncryptPassword() {
        String result = null;
        try {
            result = EncryptAction.encrypt("", "12");
        } catch (ProjectException e) {
            LOG.error("Test exception", e);
        }
        System.out.println("Encrypted password: " + result);
        System.out.println("Encrypted length  : " + result.length());
    }
}