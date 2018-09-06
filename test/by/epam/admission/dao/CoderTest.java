package by.epam.admission.dao;

import org.testng.annotations.Test;

public class CoderTest {



    @Test
    public void testEncryptPassword() {
        Coder coder = new Coder();
        String result = coder.encrypt("", "12");
        System.out.println("Encrypted password: " + result);
        System.out.println("Encrypted length  : " + result.length());
    }
}