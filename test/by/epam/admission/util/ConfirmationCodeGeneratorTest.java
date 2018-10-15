package by.epam.admission.util;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ConfirmationCodeGeneratorTest {

    @Test(description = "positive test")
    public void testGenerate() {
        int length_expected = 6;
        for (int i = 0; i < 15000000; i++) {
            String code = ConfirmationCodeGenerator.generate();
            int length_actual = code.length();
            assertEquals(length_actual, length_expected);
        }
    }
}