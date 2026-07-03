package chatapp.chat_platform.notification.repository;

import chatapp.chat_platform.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Notification> findByUserIdAndReadOrderByCreatedAtDesc(Long userId, boolean read);
    Optional<Notification> findByIdAndUserId(Long id, Long userId);
    long countByUserIdAndRead(Long userId, boolean read);
}

