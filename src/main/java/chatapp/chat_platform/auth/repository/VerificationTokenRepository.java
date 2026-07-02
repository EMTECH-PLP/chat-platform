package chatapp.chat_platform.auth.repository;

import chatapp.chat_platform.auth.model.User;
import chatapp.chat_platform.auth.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {

    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByUserAndVerifiedAtIsNull(User user);

    @Query("SELECT vt FROM VerificationToken vt WHERE vt.user = :user AND vt.verifiedAt IS NULL AND vt.expiresAt > :now")
    Optional<VerificationToken> findActiveByUser(@Param("user") User user, @Param("now") LocalDateTime now);

    @Query("SELECT vt FROM VerificationToken vt WHERE vt.token = :token AND vt.verifiedAt IS NULL AND vt.expiresAt > :now")
    Optional<VerificationToken> findActiveByToken(@Param("token") String token, @Param("now") LocalDateTime now);
}