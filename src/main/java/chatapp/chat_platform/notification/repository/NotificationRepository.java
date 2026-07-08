package chatapp.chat_platform.notification.repository;

import chatapp.chat_platform.notification.model.Notification;
import chatapp.chat_platform.notification.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Notification> findByUserIdAndRead(Long userId, boolean read);

    long countByUserIdAndRead(Long userId, boolean read);

    List<Notification> findByUserIdAndType(Long userId, NotificationType type);
}
