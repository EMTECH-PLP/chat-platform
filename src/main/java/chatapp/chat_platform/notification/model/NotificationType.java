package chatapp.chat_platform.notification.model;

/**
 * Controlled set of notification types.
 * Replaces free-form String types for consistency and type safety.
 */
public enum NotificationType {

    /**
     * A new message was sent in a room the user is a member of.
     */
    NEW_MESSAGE,

    /**
     * A user joined a room the user is a member of.
     */
    ROOM_JOINED,

    /**
     * The user was mentioned in a message.
     */
    USER_MENTIONED,

    /**
     * The user received a room invite.
     */
    ROOM_INVITE,

    /**
     * System-level alert (maintenance, announcements, etc).
     */
    SYSTEM_ALERT
}
