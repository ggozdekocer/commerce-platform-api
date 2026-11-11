package tr.com.java.commerceplatform.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tr.com.java.commerceplatform.auth.dto.LoginRequest;
import tr.com.java.commerceplatform.auth.dto.SignupRequest;
import tr.com.java.commerceplatform.auth.security.JwtUtil;
import tr.com.java.commerceplatform.common.response.CommonResponse;
import tr.com.java.commerceplatform.user.entity.User;
import tr.com.java.commerceplatform.user.service.UserService;

import java.util.Map;

/** REST endpoints for signing up, logging in and verifying the current session. */
@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Sign up, log in and verify the current session")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * Registers a new user and returns the user together with a fresh JWT token.
     *
     * @param request the signup details, including store details for store owners
     * @return HTTP 201 with the user and token wrapped in a CommonResponse
     */
    @Operation(
            summary = "Register a new user",
            description = "Creates a user with the given role. When store details are sent, "
                    + "a store is created for the user as well. Returns the user and a JWT token.")
    @ApiResponse(responseCode = "201", description = "User registered, token returned")
    @ApiResponse(responseCode = "400", description = "Role id is missing or the request body is malformed")
    @ApiResponse(responseCode = "404", description = "The given role id does not exist")
    @ApiResponse(responseCode = "409", description = "The email is already registered")
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<Map<String, Object>>> signup(@RequestBody SignupRequest request) {
        User user = userService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success(buildAuthPayload(user), "User registered successfully",
                        HttpStatus.CREATED));
    }

    /**
     * Checks the credentials and returns the user together with a fresh JWT token.
     *
     * @param request the email and password of the user
     * @return HTTP 200 with the user and token wrapped in a CommonResponse
     */
    @Operation(
            summary = "Log in",
            description = "Checks the email and password and returns the user with a JWT token. "
                    + "Send the token in the Authorization header as 'Bearer <token>' on protected endpoints.")
    @ApiResponse(responseCode = "200", description = "Login successful, token returned")
    @ApiResponse(responseCode = "400", description = "The request body is missing or malformed")
    @ApiResponse(responseCode = "401", description = "The email is unknown or the password is wrong")
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<Map<String, Object>>> login(@RequestBody LoginRequest request) {
        User user = userService.login(request);
        return ResponseEntity.ok(CommonResponse.success(buildAuthPayload(user), "Login successful"));
    }

    /**
     * Verifies the current session and returns the user with a renewed JWT token.
     *
     * @param authentication the authentication of the current user, filled in by Spring Security
     * @return HTTP 200 with the user and token wrapped in a CommonResponse
     */
    @Operation(
            summary = "Verify the current session",
            description = "Checks the JWT token of the caller and returns the user with a renewed token. "
                    + "Requires a valid token.")
    @ApiResponse(responseCode = "200", description = "Session is valid, renewed token returned")
    @ApiResponse(responseCode = "403", description = "The token is missing, invalid or expired")
    @GetMapping("/verify")
    public ResponseEntity<CommonResponse<Map<String, Object>>> verify(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(CommonResponse.success(buildAuthPayload(user), "Session verified"));
    }

    /** Builds the user details and a new token that login, signup and verify all return. */
    private Map<String, Object> buildAuthPayload(User user) {
        String token = jwtUtil.generateToken(user.getEmail());
        return Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole(),
                "token", token
        );
    }
}