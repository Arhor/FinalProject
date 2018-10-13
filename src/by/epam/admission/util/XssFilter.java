/*
 * class: XssFilter
 */

package by.epam.admission.util;

/**
 * XssFilter class provides filter-method that cleans up passed string from
 * XSS expressions
 *
 * @author Burishinets Maxim
 * @version 1.0 08 Oct 2018
 */
public class XssFilter {

    private XssFilter () {}

    /**
     * Filters passed text string
     * @param text - text string to filter
     * @return cleaned up text string
     */
    public static String doFilter(String text) {
        return text.replaceAll("</?script>", "");
    }

}
