package chatapp.chat_platform.notification.service;

import chatapp.chat_platform.notification.dto.NotificationRequest;
import chatapp.chat_platform.notification.model.Notification;
import chatapp.chat_platform.notification.model.NotificationType;
import chatapp.chat_platform.notification.model.Status;
import chatapp.chat_platform.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification create(NotificationRequest request) {
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .title(request.getTitle())
                .message(request.getMessage())
                .relatedRoomId(request.getRelatedRoomId())
                .relatedMessageId(request.getRelatedMessageId())
                .build();
        return notificationRepository.save(notification);
    }

    public Notification createForUser(UUID userId, NotificationType type, String title, String message,
                                      Long relatedRoomId, Long relatedMessageId) {
        Notification notification = Notification.builder()
                .userId(userId)
                .type(type)
                .title(title)
                .message(message)
                .relatedRoomId(relatedRoomId)
                .relatedMessageId(relatedMessageId)
                .build();
        return notificationRepository.save(notification);
    }

    public List<Notification> getForUser(UUID userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getUnreadForUser(UUID userId) {
        return notificationRepository.findByUserIdAndStatus(userId, Status.UNREAD);
    }

    public long getUnreadCount(UUID userId) {
        return notificationRepository.countByUserIdAndStatus(userId, Status.UNREAD);
    }

    public Notification markRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setStatus(Status.READ);
        return notificationRepository.save(notification);
    }

    public Notification dismiss(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setStatus(Status.DISMISSED);
        return notificationRepository.save(notification);
    }

    @Transactional
    public int markAllAsRead(UUID userId) {
        return notificationRepository.markAllAsReadForUser(userId);
    }

    public void delete(UUID notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new IllegalArgumentException("Notification not found");
        }
        notificationRepository.deleteById(notificationId);
    }
}
