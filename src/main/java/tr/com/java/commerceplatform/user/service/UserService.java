package tr.com.java.commerceplatform.user.service;

import tr.com.java.commerceplatform.auth.dto.LoginRequest;
import tr.com.java.commerceplatform.auth.dto.SignupRequest;
import tr.com.java.commerceplatform.common.exception.BadRequestException;
import tr.com.java.commerceplatform.common.exception.ConflictException;
import tr.com.java.commerceplatform.common.exception.InvalidCredentialsException;
import tr.com.java.commerceplatform.common.exception.ResourceNotFoundException;
import tr.com.java.commerceplatform.user.entity.User;

/** Business operations for registering and authenticating users. */
public interface UserService {

    /**
     * Registers a new user. When store details are given, a store is created for the user as well.
     *
     * @param request the signup details
     * @return the saved user
     * @throws BadRequestException       if the role id is missing
     * @throws ConflictException         if the email is already registered
     * @throws ResourceNotFoundException if the requested role does not exist
     */
    User save(SignupRequest request);

    /**
     * Checks the credentials of a user.
     *
     * @param request the email and password
     * @return the user that belongs to the credentials
     * @throws InvalidCredentialsException if the email is unknown or the password is wrong
     */
    User login(LoginRequest request);

    /**
     * Loads the user that belongs to an email taken from a verified token.
     *
     * @param email the email of the user
     * @return the matching user
     */
    User verify(String email);
}