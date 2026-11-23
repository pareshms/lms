package com.library.service;

import com.library.model.User;
import com.library.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple token-based authentication service.
 * Tokens are UUID strings stored in an in-memory map.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;

    /** token → User  (in-memory session store) */
    private final Map<String, User> tokenStore = new ConcurrentHashMap<>();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** Validates credentials and returns a session token, or empty. */
    public Optional<String> login(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();
            tokenStore.put(token, user.get());
            return Optional.of(token);
        }
        return Optional.empty();
    }

    /** Removes the session. */
    public void logout(String token) {
        tokenStore.remove(token);
    }

    /** Returns the user associated with a token, or empty. */
    public Optional<User> getUser(String token) {
        return Optional.ofNullable(tokenStore.get(token));
    }

    public boolean isValid(String token) {
        return token != null && tokenStore.containsKey(token);
    }
}
