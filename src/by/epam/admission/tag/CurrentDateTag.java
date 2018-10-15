/*
 * class: CurrentDateTag
 */

package by.epam.admission.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * CurrentDateTag class implements custom tag for JSP that displays current
 * date depending on passed String attribute that represents current session
 * language
 *
 * @author Burishinets Maxim
 * @version 1.0 12 Oct 2018
 * @see TagSupport
 */
public class CurrentDateTag extends TagSupport {

    private static final Locale LOCALE_RU = new Locale("ru", "RU");
    private static final Locale LOCALE_EN = new Locale("en","US");

    private String currentLocale;

    /**
     * Method receives new Calendar object that represents current date and
     * displays it depending on 'currentLocale'-attribute passed to
     * corresponding tag
     */
    @Override
    public int doStartTag() throws JspException {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat;
        switch (currentLocale) {
            case "ru_RU":
                dateFormat = DateFormat.getDateInstance(DateFormat.FULL,
                        LOCALE_RU);
                break;
            case "en_US":
            default:
                dateFormat = DateFormat.getDateInstance(DateFormat.FULL,
                        LOCALE_EN);
                break;
        }
        String date = dateFormat.format(calendar.getTime());
        try {
            JspWriter out = pageContext.getOut();
            out.write(date);
        } catch (IOException e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }

    public void setCurrentLocale(String currentLocale) {
        this.currentLocale = currentLocale;
    }
}
