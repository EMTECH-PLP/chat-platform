package chatapp.chat_platform.auth.repository;

import chatapp.chat_platform.auth.model.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, UUID> {

    // This allows you to find a token record by its specific hash string
    Optional<PasswordReset> findByTokenHash(String tokenHash);
}