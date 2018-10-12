package by.epam.admission.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CurrentDateTag extends TagSupport {

    private static final Locale LOCALE_RU = new Locale("ru", "RU");
    private static final Locale LOCALE_EN = new Locale("en","US");

    private String currentLocale;

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

        String time = dateFormat.format(calendar.getTime());

        try {
            JspWriter out = pageContext.getOut();
            out.write(time);
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
