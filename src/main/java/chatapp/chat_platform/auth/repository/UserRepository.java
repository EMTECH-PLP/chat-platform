package chatapp.chat_platform.auth.repository;

import chatapp.chat_platform.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID; // 1. Import UUID

// 2. Change 'Long' to 'UUID' here
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}