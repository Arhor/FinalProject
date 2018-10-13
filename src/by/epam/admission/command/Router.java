/*
 * class: Router
 */

package by.epam.admission.command;

import org.json.JSONObject;

/**
 * Class router serves as container that contains result of executing command
 *
 * @author Burishinets Maxim
 * @version 1.0 10 Sep 2018
 */
public class Router {

    private String page;
    private Type type;
    private int errorCode;
    private JSONObject jsonObject;

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

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public enum Type {
        FORWARD,
        REDIRECT,
        AJAX,
        ERROR
    }

}
