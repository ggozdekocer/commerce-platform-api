package tr.com.java.commerceplatform.common.response;

import org.springframework.http.HttpStatus;

/**
 * Standard response wrapper returned by every API endpoint.
 *
 * @param data           the response payload, null when the request fails
 * @param successMessage a human-readable success message, null when the request fails
 * @param httpStatus     the HTTP status of the response
 * @param errorMessage   a human-readable error message, null when the request succeeds
 * @param <T>            the type of the payload
 */
public record CommonResponse<T>(
        T data,
        String successMessage,
        HttpStatus httpStatus,
        String errorMessage
) {

    /** Builds a successful response with HTTP 200. */
    public static <T> CommonResponse<T> success(T data, String message) {
        return new CommonResponse<>(data, message, HttpStatus.OK, null);
    }

    /** Builds a successful response with a custom status such as 201 CREATED. */
    public static <T> CommonResponse<T> success(T data, String message, HttpStatus status) {
        return new CommonResponse<>(data, message, status, null);
    }

    /** Builds an error response with the given status and message. */
    public static <T> CommonResponse<T> error(HttpStatus status, String message) {
        return new CommonResponse<>(null, null, status, message);
    }
}