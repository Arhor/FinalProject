package by.epam.admission.command.impl;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;

public class SessionRequestContent {

    private HashMap<String, Object> requestAttributes;
    private HashMap<String, String[]> requestParameters;
    private HashMap<String, Object> sessionAttributes;

    public void extractValues(HttpServletRequest request) {
        // TODO: implement
        Enumeration attributeName = request.getAttributeNames();
        while (attributeName.hasMoreElements()) {
            String name = (String) attributeName.nextElement();
            Object value = request.getAttribute(name);
            requestAttributes.put(name, value);
        }

        requestParameters.putAll(request.getParameterMap());

        Enumeration sessionAttributeName = request.getSession().getAttributeNames();
        while (sessionAttributeName.hasMoreElements()) {
            String name = (String) sessionAttributeName.nextElement();
            Object value = request.getAttribute(name);
            sessionAttributes.put(name, value);
        }
    }

    public void insertAttributes(HttpServletRequest request) {
        // TODO: implement
    }

}
