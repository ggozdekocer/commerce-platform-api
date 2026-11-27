package tr.com.java.commerceplatform.common.exception;

/** Thrown when a requested resource such as a product, user or order does not exist. */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}