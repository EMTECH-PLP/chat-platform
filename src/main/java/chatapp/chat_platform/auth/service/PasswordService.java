package chatapp.chat_platform.auth.service;

import chatapp.chat_platform.auth.dto.ForgotPasswordRequest;
import chatapp.chat_platform.auth.dto.ResetPasswordRequest;
import chatapp.chat_platform.auth.dto.ChangePasswordRequest;
import chatapp.chat_platform.auth.model.PasswordReset;
import chatapp.chat_platform.auth.model.User;
import chatapp.chat_platform.auth.repository.PasswordResetRepository;
import chatapp.chat_platform.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final PasswordResetRepository passwordResetRepository;
    private final UserRepository userRepository;

    @Transactional
    public void processForgotPassword(ForgotPasswordRequest request) {
        // 1. Updated to use the active soft-delete lookup method
        User user = userRepository.findByEmailAndDeletedAtIsNull(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User with this email does not exist."));

        // 2. Generate a unique raw token to send to the user
        String rawToken = UUID.randomUUID().toString();

        // 3. Create a PasswordReset record
        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setId(UUID.randomUUID());
        passwordReset.setUser(user);
        passwordReset.setTokenHash(rawToken);
        passwordReset.setExpiresAt(LocalDateTime.now().plusHours(1));

        passwordResetRepository.save(passwordReset);

        // 4. Mocking email dispatch out to console for testing in Swagger
        System.out.println("====================================================");
        System.out.println("EMAIL DISPATCH MOCK: Send to " + user.getEmail());
        System.out.println("Your password reset token is: " + rawToken);
        System.out.println("====================================================");
    }

    @Transactional
    public void processResetPassword(ResetPasswordRequest request) {
        // 1. Find the token record
        PasswordReset resetRecord = passwordResetRepository.findByTokenHash(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or non-existent token."));

        // 2. Security Checks
        if (resetRecord.getUsedAt() != null) {
            throw new IllegalStateException("This token has already been used.");
        }
        if (resetRecord.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("This token has expired.");
        }

        // 3. Update User Password
        User user = resetRecord.getUser();
        user.setPasswordHash(request.getNewPassword());
        userRepository.save(user);

        // 4. Invalidate the token so it can't be reused
        resetRecord.setUsedAt(LocalDateTime.now());
        passwordResetRepository.save(resetRecord);
    }

    @Transactional
    public void processChangePassword(String username, ChangePasswordRequest request) {
        // 1. Updated to use the active soft-delete lookup method
        User user = userRepository.findByUsernameAndDeletedAtIsNull(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        // 2. Verify current password matches database string
        if (!user.getPasswordHash().equals(request.getCurrentPassword())) {
            throw new IllegalArgumentException("Your current password does not match our records.");
        }

        // 3. Update to new password
        user.setPasswordHash(request.getNewPassword());
        userRepository.save(user);
    }
}