/*
 * class: Router
 */

package by.epam.admission.command;

/**
 * @author Burishinets Maxim
 * @version 1.0 10 Sep 2018
 */
public class Router {

    private String page;
    private Type type;
    private int errorCode;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public enum Type {
        FORWARD,
        REDIRECT,
        ERROR
    }

}
