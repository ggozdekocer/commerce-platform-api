package tr.com.java.commerceplatform.common.exception;

/** Thrown when a request is missing required data or contains invalid values. */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}