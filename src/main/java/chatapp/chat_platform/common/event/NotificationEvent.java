package chatapp.chat_platform.common.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Base class for all notification-triggering events.
 * Any module can publish subclasses of this event to trigger notifications.
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class NotificationEvent {

    /**
     * The user who should receive the notification.
     */
    private Long targetUserId;

    /**
     * The type of notification (maps to NotificationType enum).
     */
    private String type;

    /**
     * When the event occurred.
     */
    private LocalDateTime timestamp;

    /**
     * Human-readable message for the notification.
     */
    private String message;

    /**
     * Optional: the room related to this event.
     */
    private Long relatedRoomId;

    /**
     * Optional: the message related to this event.
     */
    private Long relatedMessageId;
}
