package chatapp.chat_platform.auth.service;

import chatapp.chat_platform.auth.config.JwtUtil;
import chatapp.chat_platform.auth.dto.request.LoginRequest;
import chatapp.chat_platform.auth.dto.request.RegisterRequest;
import chatapp.chat_platform.auth.dto.response.AuthResponse;
import chatapp.chat_platform.auth.model.Role;
import chatapp.chat_platform.auth.model.User;
import chatapp.chat_platform.auth.repository.UserRepository;
import chatapp.chat_platform.auth.util.AuthConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final VerificationService verificationService;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        logger.info("Registering new user: {}", request.getUsername());

        if (userRepository.existsActiveByUsername(request.getUsername())) {
            throw new RuntimeException(AuthConstants.MSG_USERNAME_TAKEN);
        }

        if (userRepository.existsActiveByEmail(request.getEmail())) {
            throw new RuntimeException(AuthConstants.MSG_EMAIL_REGISTERED);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setEmailVerified(false);
        user.setEnabled(false);

        user = userRepository.save(user);

        verificationService.generateAndSendVerificationToken(user);

        return new AuthResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole().name(),
            null,
            null,
            0L
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        logger.info("Login attempt for user: {}", request.getUsername());

        User user = userRepository.findByUsernameAndDeletedAtIsNull(request.getUsername())
                .orElseThrow(() -> new RuntimeException(AuthConstants.MSG_INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException(AuthConstants.MSG_INVALID_CREDENTIALS);
        }

        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new RuntimeException(AuthConstants.MSG_EMAIL_NOT_VERIFIED);
        }

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new RuntimeException(AuthConstants.MSG_ACCOUNT_NOT_ACTIVATED);
        }

        logger.info("User logged in successfully: {}", user.getUsername());

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        return new AuthResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole().name(),
            accessToken,
            refreshToken,
            expiration
        );
    }
}