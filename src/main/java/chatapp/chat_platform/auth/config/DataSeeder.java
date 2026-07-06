package chatapp.chat_platform.auth.config;

import chatapp.chat_platform.auth.model.Role;
import chatapp.chat_platform.auth.model.User;
import chatapp.chat_platform.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsActiveByUsername("Admin_User")) {
            User admin = new User();
            admin.setUsername("Admin_User");
            admin.setEmail("admin@chatplatform.com");
            admin.setPasswordHash(passwordEncoder.encode("Admin@123"));
            admin.setDisplayName("System Admin");
            admin.setRole(Role.ADMIN);
            admin.setEmailVerified(true);
            admin.setEnabled(true);

            userRepository.save(admin);
            logger.info("Admin user seeded successfully");
        } else {
            logger.info("Admin user already exists, skipping seed");
        }
    }
}
