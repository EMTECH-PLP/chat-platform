package chatapp.chat_platform.common.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Published when a user joins a chat room.
 * One event per existing room member to notify them.
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RoomJoinedEvent extends NotificationEvent {

    /**
     * The room that was joined.
     */
    private Long roomId;

    /**
     * The user who joined the room.
     */
    private Long joinedUserId;

    /**
     * The name of the room.
     */
    private String roomName;
}
