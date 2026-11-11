package tr.com.java.commerceplatform.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tr.com.java.commerceplatform.common.response.CommonResponse;

/** Catches exceptions thrown by controllers and converts them into a CommonResponse. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Handles requests for a record that does not exist and answers with HTTP 404. */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CommonResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** Handles requests with missing or invalid data and answers with HTTP 400. */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CommonResponse<Void>> handleBadRequest(BadRequestException ex) {
        log.warn("Bad request: {}", ex.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /** Handles request bodies that are empty or are not valid JSON and answers with HTTP 400. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse<Void>> handleUnreadableBody(HttpMessageNotReadableException ex) {
        log.warn("Request body could not be read: {}", ex.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, "Request body is missing or malformed");
    }

    /** Handles failed logins and answers with HTTP 401. */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<CommonResponse<Void>> handleInvalidCredentials(InvalidCredentialsException ex) {
        log.warn("Authentication failed: {}", ex.getMessage());
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    /** Wraps a status and message into the standard error response. */
    private ResponseEntity<CommonResponse<Void>> buildError(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(CommonResponse.error(status, message));
    }

    /** Handles requests that conflict with existing data and answers with HTTP 409. */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<CommonResponse<Void>> handleConflict(ConflictException ex) {
        log.warn("Conflict: {}", ex.getMessage());
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Handles orders that exceed the available stock and answers with HTTP 409. */
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<CommonResponse<Void>> handleInsufficientStock(InsufficientStockException ex) {
        log.warn("Insufficient stock: {}", ex.getMessage());
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }
}