package by.epam.admission.util;

import by.epam.admission.util.EncryptAction;
import org.testng.annotations.Test;

public class EncryptActionTest {



    @Test
    public void testEncryptPassword() {
        EncryptAction encryptAction = new EncryptAction();
        String result = encryptAction.encrypt("", "12");
        System.out.println("Encrypted password: " + result);
        System.out.println("Encrypted length  : " + result.length());
    }
}