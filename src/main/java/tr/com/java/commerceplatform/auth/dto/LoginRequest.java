package tr.com.java.commerceplatform.auth.dto;

/**
 * Request body for the login endpoint.
 *
 * @param email    the email address of the user
 * @param password the raw password of the user
 */
public record LoginRequest(String email, String password) {
}