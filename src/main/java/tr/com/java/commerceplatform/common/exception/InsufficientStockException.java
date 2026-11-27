package tr.com.java.commerceplatform.common.exception;

/** Thrown when an order asks for more items than a product has in stock. */
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }
}