/*
 * class: Router
 */

package by.epam.admission.command;

import org.json.JSONObject;

/**
 * Class Router serves as container that encapsulates result of executing
 * command
 *
 * @author Burishinets Maxim
 * @version 1.0 10 Sep 2018
 */
public class Router {

    private String page;
    private Type type;
    private int errorCode;
    private JSONObject jsonObject;

    /**
     * Method returns encapsulated destination page as result of executing
     * command
     *
     * @return destination page URI
     */
    public String getPage() {
        return page;
    }


    /**
     * Method sets destination page URI if command execution was successful
     *
     * @param page - destination page URI
     */
    public void setPage(String page) {
        this.page = page;
    }

    /**
     * Method returns type that determines further actions on the object
     *
     * @return type that defines the method of processing encapsulated data
     */
    public Type getType() {
        return type;
    }

    /**
     * Method sets type that determines further actions on the object
     *
     * @param type - type that defines the method of processing encapsulated
     * data
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Method returns encapsulated HTTP Status code for concrete error
     *
     * @return HTTP Status code for concrete error
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Method sets encapsulated HTTP Status code for concrete error
     *
     * @param errorCode - HTTP Status code for concrete error
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Method returns JSON object that encapsulates data to be sent with
     * HTTP response
     *
     * @return JSON object that encapsulates data to be sent with HTTP response
     */
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    /**
     * Method sets JSON object that encapsulates data to be sent with HTTP
     * response
     *
     * @param jsonObject - JSON object that encapsulates data to be sent with
     * HTTP response
     */
    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    /**
     * Class Type contains possible types for Router objects
     * @author Maxim Burishinets
     * @version 1.0 10 Sep 2018
     */
    public enum Type {
        FORWARD,
        REDIRECT,
        AJAX,
        ERROR
    }

}
