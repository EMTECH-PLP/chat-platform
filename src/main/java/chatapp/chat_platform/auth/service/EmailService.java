package chatapp.chat_platform.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String to, String username, String token) {
        String verificationLink = frontendUrl + "/verify-email?token=" + token;

        // Console log for dev visibility
        logger.info("========================================");
        logger.info("📧 SENDING VERIFICATION EMAIL");
        logger.info("To: {}", to);
        logger.info("Subject: Verify Your Email - Chat Platform");
        logger.info("Hello {},", username);
        logger.info("🔗 VERIFICATION LINK: {}", verificationLink);
        logger.info("========================================");

        // Send actual email
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Verify Your Email - Chat Platform");
            helper.setText(buildEmailBody(username, verificationLink), true);

            mailSender.send(message);
            logger.info("Verification email sent to {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send verification email to {}: {}", to, e.getMessage());
        }
    }

    public void sendPasswordResetEmail(String to, String username, String token) {
        String resetLink = frontendUrl + "/reset-password?token=" + token;

        // Console log for dev visibility
        logger.info("========================================");
        logger.info("📧 SENDING PASSWORD RESET EMAIL");
        logger.info("To: {}", to);
        logger.info("🔗 RESET LINK: {}", resetLink);
        logger.info("Token: {}", token);
        logger.info("========================================");

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Password Reset Request - Chat Platform");
            helper.setText(buildPasswordResetBody(username, resetLink, token), true);

            mailSender.send(message);
            logger.info("Password reset email sent to {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send password reset email to {}: {}", to, e.getMessage());
        }
    }

    private String buildEmailBody(String username, String verificationLink) {
        return """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto;">
                    <h2 style="color: #4A90E2;">Welcome to Chat Platform!</h2>
                    <p>Hello <strong>%s</strong>,</p>
                    <p>Thank you for registering. Please verify your email address by clicking the button below:</p>
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s"
                           style="background-color: #4A90E2; color: white; padding: 12px 24px;
                                  text-decoration: none; border-radius: 4px; font-size: 16px;">
                            Verify Email
                        </a>
                    </div>
                    <p>Or copy and paste this link into your browser:</p>
                    <p style="color: #666;">%s</p>
                    <p style="color: #999; font-size: 12px;">This link expires in 24 hours. If you did not create an account, ignore this email.</p>
                </div>
                """.formatted(username, verificationLink, verificationLink);
    }

    private String buildPasswordResetBody(String username, String resetLink, String token) {
        return """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto;">
                    <h2 style="color: #E24A4A;">Password Reset Request</h2>
                    <p>Hello <strong>%s</strong>,</p>
                    <p>We received a request to reset your password. Click the button below to proceed:</p>
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s"
                           style="background-color: #E24A4A; color: white; padding: 12px 24px;
                                  text-decoration: none; border-radius: 4px; font-size: 16px;">
                            Reset Password
                        </a>
                    </div>
                    <p>Or use this token directly via the API:</p>
                    <p style="background: #f4f4f4; padding: 10px; font-family: monospace; word-break: break-all;">%s</p>
                    <p style="color: #999; font-size: 12px;">This link expires in 1 hour. If you did not request a password reset, ignore this email.</p>
                </div>
                """.formatted(username, resetLink, token);
    }
}
