package chatapp.chat_platform.common.event;

// Fired by chat whenever a message is saved. Chat has no idea who listens
// to this - it just announces it happened.
public record MessageCreatedEvent(
        Long messageId,
        Long roomId,
        Long senderId,
        String content
) {
}