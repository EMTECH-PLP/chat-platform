package chatapp.chat_platform.auth.service;

import chatapp.chat_platform.auth.dto.LoginRequest;
import chatapp.chat_platform.auth.dto.RegisterRequest;
import chatapp.chat_platform.auth.model.User;
import chatapp.chat_platform.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(request.getPassword()); // TODO: encode password
        user.setDisplayName(request.getDisplayName());
        return userRepository.save(user);
    }

    public User login(LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .filter(u -> u.getPasswordHash().equals(request.getPassword())) // TODO: password check
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
    }
}
