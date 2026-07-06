package chatapp.chat_platform.auth.service;

import chatapp.chat_platform.auth.dto.request.AdminUserRequest;
import chatapp.chat_platform.auth.model.User;
import chatapp.chat_platform.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void deleteUser(UUID targetId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(targetId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public User updateUser(UUID targetId, AdminUserRequest request) {
        User user = userRepository.findByIdAndDeletedAtIsNull(targetId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (request.getUsername() != null) {
            if (userRepository.existsActiveByUsername(request.getUsername())) {
                throw new IllegalArgumentException("Username already taken");
            }
            user.setUsername(request.getUsername());
        }
        if (request.getEmail() != null) {
            if (userRepository.existsActiveByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already registered");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getDisplayName() != null) user.setDisplayName(request.getDisplayName());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());
        if (request.getPassword() != null) user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }
}
