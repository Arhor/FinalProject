/*
 * class: DaoException
 */

package by.epam.admission.exception;

/**
 * @author Maxim Burishinets
 * @version 1.0 1 Sep 2018
 */
public class DaoException extends Exception {

    public DaoException() {
        super();
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}