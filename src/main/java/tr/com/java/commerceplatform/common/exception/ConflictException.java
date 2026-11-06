package tr.com.java.commerceplatform.common.exception;

/** Thrown when a request conflicts with existing data, for example a duplicate email. */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}