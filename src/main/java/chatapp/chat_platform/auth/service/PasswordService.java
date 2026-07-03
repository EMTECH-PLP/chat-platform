package chatapp.chat_platform.auth.service;

import chatapp.chat_platform.auth.dto.ForgotPasswordRequest;
import chatapp.chat_platform.auth.dto.ResetPasswordRequest;
import chatapp.chat_platform.auth.dto.ChangePasswordRequest;
import chatapp.chat_platform.auth.model.PasswordReset;
import chatapp.chat_platform.auth.model.User;
import chatapp.chat_platform.auth.repository.PasswordResetRepository;
import chatapp.chat_platform.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final PasswordResetRepository passwordResetRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public void processForgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmailAndDeletedAtIsNull(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User with this email does not exist."));

        String rawToken = UUID.randomUUID().toString();

        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setUser(user);
        passwordReset.setTokenHash(rawToken);
        passwordReset.setExpiresAt(LocalDateTime.now().plusHours(1));

        passwordResetRepository.save(passwordReset);

        emailService.sendPasswordResetEmail(user.getEmail(), user.getUsername(), rawToken);
    }

    @Transactional
    public void processResetPassword(ResetPasswordRequest request) {
        PasswordReset resetRecord = passwordResetRepository.findByTokenHash(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or non-existent token."));

        if (resetRecord.getUsedAt() != null) {
            throw new IllegalStateException("This token has already been used.");
        }
        if (resetRecord.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("This token has expired.");
        }

        User user = resetRecord.getUser();
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        resetRecord.setUsedAt(LocalDateTime.now());
        passwordResetRepository.save(resetRecord);
    }

    @Transactional
    public void processChangePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsernameAndDeletedAtIsNull(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Your current password does not match our records.");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}