package chatapp.chat_platform.common.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Published when a user is mentioned (@username) in a message.
 * Future use — ready for when mention detection is implemented.
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserMentionedEvent extends NotificationEvent {

    /**
     * The room where the mention occurred.
     */
    private Long roomId;

    /**
     * The user who mentioned them.
     */
    private Long mentionedByUserId;

    /**
     * The message ID containing the mention.
     */
    private Long messageId;

    /**
     * The room name (for display).
     */
    private String roomName;
}
