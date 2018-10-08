/*
 * class: XssFilter
 */

package by.epam.admission.util;

/**
 * @author Burishinets Maxim
 * @version 1.0 08 Oct 2018
 */
public class XssFilter {

    private XssFilter () {}

    public static String doFilter(String text) {
        return text.replaceAll("</?script>", "");
    }

}
