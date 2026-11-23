package com.library.controller;

import com.library.model.User;
import com.library.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username and password required"));
        }

        Optional<String> token = authService.login(username, password);
        if (token.isPresent()) {
            User user = authService.getUser(token.get()).orElseThrow();
            return ResponseEntity.ok(Map.of(
                    "token", token.get(),
                    "name",  user.getName(),
                    "role",  user.getUserType() != null ? user.getUserType() : "LIBRARIAN"
            ));
        }
        return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String auth) {
        if (auth != null && auth.startsWith("Bearer ")) {
            authService.logout(auth.substring(7));
        }
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }
}
