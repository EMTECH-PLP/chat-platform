package chatapp.chat_platform.notification.repository;

import chatapp.chat_platform.notification.model.Notification;
import chatapp.chat_platform.notification.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<Notification> findByUserIdAndStatus(UUID userId, Status status);

    long countByUserIdAndStatus(UUID userId, Status status);

    @Modifying
    @Query("UPDATE Notification n SET n.status = 'READ' WHERE n.userId = :userId AND n.status = 'UNREAD'")
    int markAllAsReadForUser(@Param("userId") UUID userId);
}
