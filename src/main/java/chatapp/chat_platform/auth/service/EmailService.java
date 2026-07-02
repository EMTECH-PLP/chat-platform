package chatapp.chat_platform.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    public void sendVerificationEmail(String to, String username, String token) {
        String verificationLink = frontendUrl + "/verify-email?token=" + token;

        logger.info("========================================");
        logger.info("📧 SENDING VERIFICATION EMAIL");
        logger.info("To: {}", to);
        logger.info("Subject: Verify Your Email - Chat Platform");
        logger.info("Body:");
        logger.info("Hello {},", username);
        logger.info("Welcome to Chat Platform! Please verify your email:");
        logger.info(verificationLink);
        logger.info("This link expires in 24 hours.");
        logger.info("========================================");
        logger.info("🔗 VERIFICATION LINK: {}", verificationLink);
        logger.info("========================================");
    }
}