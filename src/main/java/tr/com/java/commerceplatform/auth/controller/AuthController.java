package tr.com.java.commerceplatform.auth.controller;

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
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * Registers a new user and returns the user together with a fresh JWT token.
     *
     * @param request the signup details, including store details for store owners
     * @return HTTP 201 with the user and token wrapped in a CommonResponse
     */
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