package chatapp.chat_platform.notification.service;

import chatapp.chat_platform.common.event.MessageSentEvent;
import chatapp.chat_platform.common.event.NotificationEvent;
import chatapp.chat_platform.common.event.RoomJoinedEvent;
import chatapp.chat_platform.common.event.UserMentionedEvent;
import chatapp.chat_platform.notification.dto.NotificationRequest;
import chatapp.chat_platform.notification.model.Notification;
import chatapp.chat_platform.notification.model.NotificationType;
import chatapp.chat_platform.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    /**
     * Create a notification from a direct REST request (manual trigger).
     */
    public Notification create(NotificationRequest request) {
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .message(request.getMessage())
                .relatedRoomId(request.getRelatedRoomId())
                .relatedMessageId(request.getRelatedMessageId())
                .build();
        return notificationRepository.save(notification);
    }

    /**
     * Create a notification from a domain event.
     * This is the primary method used by the event listener for
     * inter-service communication.
     */
    public Notification createFromEvent(NotificationEvent event) {
        NotificationType notificationType = resolveType(event);

        Notification notification = Notification.builder()
                .userId(event.getTargetUserId())
                .type(notificationType)
                .message(event.getMessage())
                .relatedRoomId(event.getRelatedRoomId())
                .relatedMessageId(event.getRelatedMessageId())
                .build();

        Notification saved = notificationRepository.save(notification);
        logger.debug("Notification created: type={}, userId={}, id={}",
                notificationType, event.getTargetUserId(), saved.getId());
        return saved;
    }

    public List<Notification> getForUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getUnreadForUser(Long userId) {
        return notificationRepository.findByUserIdAndRead(userId, false);
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndRead(userId, false);
    }

    public Notification markRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    public void markAllReadForUser(Long userId) {
        List<Notification> unread = notificationRepository.findByUserIdAndRead(userId, false);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
        logger.debug("Marked {} notifications as read for userId={}", unread.size(), userId);
    }

    /**
     * Maps event type to the NotificationType enum.
     */
    private NotificationType resolveType(NotificationEvent event) {
        if (event instanceof MessageSentEvent) {
            return NotificationType.NEW_MESSAGE;
        } else if (event instanceof RoomJoinedEvent) {
            return NotificationType.ROOM_JOINED;
        } else if (event instanceof UserMentionedEvent) {
            return NotificationType.USER_MENTIONED;
        }
        // Fallback — parse from the event's type string
        try {
            return NotificationType.valueOf(event.getType());
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.warn("Unknown notification type from event: {}, defaulting to SYSTEM_ALERT", event.getType());
            return NotificationType.SYSTEM_ALERT;
        }
    }
}
