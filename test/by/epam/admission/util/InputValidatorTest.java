package by.epam.admission.util;

import by.epam.admission.exception.ProjectException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class InputValidatorTest {

    @Test(dataProvider = "valid params", description = "positive test")
    public void testValidatePositive(String param, InputValidator.InputType type)
            throws ProjectException {
        boolean result = InputValidator.validate(param, type);
        Assert.assertTrue(result);
    }

    @Test(dataProvider = "invalid params", description = "negative test")
    public void testValidateNegative(String param, InputValidator.InputType type)
            throws ProjectException {
        boolean result = InputValidator.validate(param, type);
        Assert.assertFalse(result);
    }

    @DataProvider(name = "valid params")
    public Object[][] createValidData() {
        return new Object[][] {
                {"1", InputValidator.InputType.CERTIFICATE},
                {"11", InputValidator.InputType.CERTIFICATE},
                {"22", InputValidator.InputType.CERTIFICATE},
                {"33", InputValidator.InputType.CERTIFICATE},
                {"44", InputValidator.InputType.CERTIFICATE},
                {"55", InputValidator.InputType.CERTIFICATE},
                {"10", InputValidator.InputType.CERTIFICATE},
                {"20", InputValidator.InputType.CERTIFICATE},
                {"99", InputValidator.InputType.CERTIFICATE},
                {"100", InputValidator.InputType.CERTIFICATE},
                {"Io", InputValidator.InputType.COUNTRY},
                {"Russia", InputValidator.InputType.COUNTRY},
                {"Россия", InputValidator.InputType.COUNTRY},
                {"USA", InputValidator.InputType.COUNTRY},
                {"Германия", InputValidator.InputType.COUNTRY},
                {"Минск", InputValidator.InputType.CITY},
                {"Minsk", InputValidator.InputType.CITY},
                {"Los-Angeles", InputValidator.InputType.CITY},
                {"Алма-Аты", InputValidator.InputType.CITY},
                {"user.name@com", InputValidator.InputType.EMAIL},
                {"user@io.org", InputValidator.InputType.EMAIL},
                {"test-email@testdomain.test", InputValidator.InputType.EMAIL},
                {"io@io", InputValidator.InputType.EMAIL},
                {"TEST1test", InputValidator.InputType.PASSWORD},
                {"ThD8H9", InputValidator.InputType.PASSWORD},
                {"rop69ROP", InputValidator.InputType.PASSWORD},
                {"Max", InputValidator.InputType.FIRST_NAME},
                {"Jack", InputValidator.InputType.FIRST_NAME},
                {"Anne", InputValidator.InputType.FIRST_NAME},
                {"Антон", InputValidator.InputType.FIRST_NAME},
                {"Ирина", InputValidator.InputType.FIRST_NAME},
                {"Виктор", InputValidator.InputType.FIRST_NAME},
                {"Fox", InputValidator.InputType.LAST_NAME},
                {"White", InputValidator.InputType.LAST_NAME},
                {"VeryLongLastName", InputValidator.InputType.LAST_NAME}
        };
    }

    @DataProvider(name = "invalid params")
    public Object[][] createInvalidData() {
        return new Object[][] {
                {"01", InputValidator.InputType.CERTIFICATE},
                {"111", InputValidator.InputType.CERTIFICATE},
                {"0100", InputValidator.InputType.CERTIFICATE},
                {"Io1", InputValidator.InputType.COUNTRY},
                {"Rus sia", InputValidator.InputType.COUNTRY},
                {" Россия", InputValidator.InputType.COUNTRY},
                {"USA ", InputValidator.InputType.COUNTRY},
                {"SvLSucAyRFvKmkEzIcsDcwoqjYXtFVBjEdRLjtRwbRDnFtABWzqKdcXzBrku", InputValidator.InputType.COUNTRY},
                {"-Минск", InputValidator.InputType.CITY},
                {"Minsk-", InputValidator.InputType.CITY},
                {"XeTWwIKolJRmLWzSGccioSaTzOkpYHgknMltUekIKZuCoqpdPwBLEkUkxeQQ", InputValidator.InputType.CITY},
                {"user@YtkuufGaTqsvgflrYpqctAtGmZIGarfuRLZZQSyoGCcSZnaYDpYqTNRwuRkgGdIDdJITmK.org", InputValidator.InputType.EMAIL},
                {"1", InputValidator.InputType.PASSWORD},
                {" ", InputValidator.InputType.PASSWORD},
                {"1DjkdifgJIjodjoifgj1", InputValidator.InputType.PASSWORD},
                {"CpGasLGCbpxlIvFENwsLnOAGavpkhKnvWSgLZobNPvKNjtBGaMVctckbHyOAvbhJOKVylV", InputValidator.InputType.FIRST_NAME},
                {"LqtYGklbMxtxuqfwDhFDGlATTUkfSsOUAjxUhhvkajXifwGFaNZnYiHZZvsKMYqPCYePyR", InputValidator.InputType.LAST_NAME}
        };
    }
}