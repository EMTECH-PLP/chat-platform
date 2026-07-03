package chatapp.chat_platform.notification.service;

import chatapp.chat_platform.chat.model.Message;
import chatapp.chat_platform.chat.model.Room;
import chatapp.chat_platform.notification.dto.NotificationRequest;
import chatapp.chat_platform.notification.model.Notification;
import chatapp.chat_platform.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private static final int MESSAGE_PREVIEW_LENGTH = 80;

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

    public List<Notification> createMessageNotifications(Room room, Message message) {
        return createForUsers(
                room.getMemberIds().stream()
                        .filter(userId -> !userId.equals(message.getSenderId()))
                        .toList(),
                "MESSAGE",
                "New message in " + room.getName() + ": " + preview(message.getContent()),
                room.getId(),
                message.getId()
        );
    }

    public List<Notification> createForUsers(Collection<Long> userIds,
                                             String type,
                                             String message,
                                             Long relatedRoomId,
                                             Long relatedMessageId) {
        List<Notification> notifications = userIds.stream()
                .distinct()
                .map(userId -> Notification.builder()
                        .userId(userId)
                        .type(type)
                        .message(message)
                        .relatedRoomId(relatedRoomId)
                        .relatedMessageId(relatedMessageId)
                        .build())
                .toList();
        return notificationRepository.saveAll(notifications);
    }

    @Transactional(readOnly = true)
    public List<Notification> getForUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional(readOnly = true)
    public List<Notification> getUnreadForUser(Long userId) {
        return notificationRepository.findByUserIdAndReadOrderByCreatedAtDesc(userId, false);
    }

    @Transactional(readOnly = true)
    public long countUnreadForUser(Long userId) {
        return notificationRepository.countByUserIdAndRead(userId, false);
    }

    public Notification markRead(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    public List<Notification> markAllRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndReadOrderByCreatedAtDesc(userId, false);
        unreadNotifications.forEach(notification -> notification.setRead(true));
        return notificationRepository.saveAll(unreadNotifications);
    }

    public void delete(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notificationRepository.delete(notification);
    }

    private String preview(String content) {
        if (content.length() <= MESSAGE_PREVIEW_LENGTH) {
            return content;
        }
        return content.substring(0, MESSAGE_PREVIEW_LENGTH) + "...";
    }
}
