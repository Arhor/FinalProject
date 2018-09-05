/*
 * class: DAOException
 */

package by.epam.admission.exception;

/**
 * @author Maxim Burishinets
 * @version 1.0 1 Sep 2018
 */
public class DAOException extends Exception {

    public DAOException() {
        super();
    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}