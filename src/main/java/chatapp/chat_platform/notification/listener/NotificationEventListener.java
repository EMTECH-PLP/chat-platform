package chatapp.chat_platform.notification.listener;

import chatapp.chat_platform.common.event.MessageSentEvent;
import chatapp.chat_platform.common.event.RoomJoinedEvent;
import chatapp.chat_platform.common.event.UserMentionedEvent;
import chatapp.chat_platform.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listens for domain events published by other modules and creates
 * notifications accordingly. Processing is async so the publisher
 * is never blocked by notification persistence.
 */
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private static final Logger logger = LoggerFactory.getLogger(NotificationEventListener.class);

    private final NotificationService notificationService;

    /**
     * Handle new message events.
     * Creates a notification for the target user about the new message.
     */
    @Async("notificationExecutor")
    @EventListener
    public void handleMessageSent(MessageSentEvent event) {
        logger.debug("Handling MessageSentEvent: roomId={}, targetUser={}", 
                event.getRoomId(), event.getTargetUserId());
        try {
            notificationService.createFromEvent(event);
        } catch (Exception e) {
            logger.error("Failed to process MessageSentEvent for userId={}: {}",
                    event.getTargetUserId(), e.getMessage(), e);
        }
    }

    /**
     * Handle room joined events.
     * Notifies existing room members that someone new joined.
     */
    @Async("notificationExecutor")
    @EventListener
    public void handleRoomJoined(RoomJoinedEvent event) {
        logger.debug("Handling RoomJoinedEvent: roomId={}, joinedUser={}, targetUser={}",
                event.getRoomId(), event.getJoinedUserId(), event.getTargetUserId());
        try {
            notificationService.createFromEvent(event);
        } catch (Exception e) {
            logger.error("Failed to process RoomJoinedEvent for userId={}: {}",
                    event.getTargetUserId(), e.getMessage(), e);
        }
    }

    /**
     * Handle user mentioned events.
     * Notifies the mentioned user.
     */
    @Async("notificationExecutor")
    @EventListener
    public void handleUserMentioned(UserMentionedEvent event) {
        logger.debug("Handling UserMentionedEvent: targetUser={}, mentionedBy={}",
                event.getTargetUserId(), event.getMentionedByUserId());
        try {
            notificationService.createFromEvent(event);
        } catch (Exception e) {
            logger.error("Failed to process UserMentionedEvent for userId={}: {}",
                    event.getTargetUserId(), e.getMessage(), e);
        }
    }
}
