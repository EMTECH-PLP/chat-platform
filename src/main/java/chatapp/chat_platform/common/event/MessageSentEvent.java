package chatapp.chat_platform.common.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Published when a message is sent in a chat room.
 * One event per recipient (excluding the sender).
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSentEvent extends NotificationEvent {

    /**
     * The ID of the room where the message was sent.
     */
    private Long roomId;

    /**
     * The user who sent the message.
     */
    private Long senderId;

    /**
     * The persisted message ID.
     */
    private Long messageId;

    /**
     * The name of the room (for display in notification text).
     */
    private String roomName;

    /**
     * A preview of the message content (first 50 characters).
     */
    private String contentPreview;
}
