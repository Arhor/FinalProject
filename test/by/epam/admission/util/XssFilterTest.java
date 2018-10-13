/*
 * class: XssFilterTest
 */

package by.epam.admission.util;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Burishinets Maxim
 * @version 1.0 12 Oct 2018
 */
public class XssFilterTest {

    @Test(dataProvider = "XSS examples", description = "positive test")
    public void testDoFilter(String text) {
        String filtered = XssFilter.doFilter(text);
        Pattern pattern = Pattern.compile("</?script>");
        Matcher matcher = pattern.matcher(filtered);
        Assert.assertFalse(matcher.find());
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }

    @DataProvider(name = "XSS examples")
    public Object[][] createData() {
        return new Object[][]{
            {"<script>alert('bang!');</script>"}
        };
    }

}