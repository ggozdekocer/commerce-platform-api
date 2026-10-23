package tr.com.java.commerceplatform.auth.controller;

import tr.com.java.commerceplatform.auth.dto.LoginRequest;
import tr.com.java.commerceplatform.auth.dto.SignupRequest;
import tr.com.java.commerceplatform.user.entity.User;
import tr.com.java.commerceplatform.auth.security.JwtUtil;
import tr.com.java.commerceplatform.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestBody SignupRequest request) {
        User user = userService.save(request);
        String token = jwtUtil.generateToken(user.getEmail());
        return Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole(),
                "token", token
        );
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest request) {
        User user = userService.login(request);
        String token = jwtUtil.generateToken(user.getEmail());
        return Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole(),
                "token", token
        );
    }

    @GetMapping("/verify")
    public Map<String, Object> verify(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
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