/*
 * class: ProjectException
 */

package by.epam.admission.exception;

/**
 * Represents various exceptions that may occur during application running
 *
 * @author Maxim Burishinets
 * @version 1.0 1 Sep 2018
 */
public class ProjectException extends Exception {

    public ProjectException() {
        super();
    }

    public ProjectException(String message) {
        super(message);
    }

    public ProjectException(String message, Throwable cause) {
        super(message, cause);
    }
}