/*
 * class: NotSupportedOperationException
 */

package by.epam.admission.exception;

/**
 * @author Maxim Burishinets
 * @version 1.0 3 Sep 2018
 */
public class NotSupportedOperationException extends Throwable {

    public NotSupportedOperationException() {
        super();
    }

    public NotSupportedOperationException(String message) {
        super(message);
    }

    public NotSupportedOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
