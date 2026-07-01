package chatapp.chat_platform.notification.service;

import chatapp.chat_platform.notification.dto.NotificationRequest;
import chatapp.chat_platform.notification.model.Notification;
import chatapp.chat_platform.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

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

    public List<Notification> getForUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getUnreadForUser(Long userId) {
        return notificationRepository.findByUserIdAndRead(userId, false);
    }

    public Notification markRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }
}
