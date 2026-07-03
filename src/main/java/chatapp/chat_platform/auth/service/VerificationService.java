package chatapp.chat_platform.auth.service;

import chatapp.chat_platform.auth.model.User;
import chatapp.chat_platform.auth.model.VerificationToken;
import chatapp.chat_platform.auth.repository.UserRepository;
import chatapp.chat_platform.auth.repository.VerificationTokenRepository;
import chatapp.chat_platform.auth.util.AuthConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private static final Logger logger = LoggerFactory.getLogger(VerificationService.class);
    private static final SecureRandom secureRandom = new SecureRandom();

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Transactional
    public void generateAndSendVerificationToken(User user) {
        verificationTokenRepository.findByUserAndVerifiedAtIsNull(user)
                .ifPresent(existingToken -> {
                    verificationTokenRepository.delete(existingToken);
                    logger.info("Deleted existing verification token for user: {}", user.getUsername());
                });

        String token = generateRandomToken();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(LocalDateTime.now().plusHours(AuthConstants.TOKEN_EXPIRY_HOURS));

        verificationTokenRepository.save(verificationToken);

        emailService.sendVerificationEmail(user.getEmail(), user.getUsername(), token);
        logger.info("Verification token generated for user: {}", user.getUsername());
    }

    @Transactional
    public User verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository
                .findActiveByToken(token, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException(AuthConstants.MSG_INVALID_TOKEN));

        User user = verificationToken.getUser();

        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new RuntimeException("Email already verified");
        }

        verificationToken.setVerifiedAt(LocalDateTime.now());
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setVerifiedAt(LocalDateTime.now());

        userRepository.save(user);
        verificationTokenRepository.save(verificationToken);

        logger.info("Email verified for user: {}", user.getUsername());
        return user;
    }

    @Transactional
    public void resendVerification(String email) {
        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new RuntimeException("Email already verified");
        }

        generateAndSendVerificationToken(user);
        logger.info("Verification email resent to: {}", email);
    }

    public boolean isEmailVerified(UUID userId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return Boolean.TRUE.equals(user.getEmailVerified());
    }

    private String generateRandomToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}