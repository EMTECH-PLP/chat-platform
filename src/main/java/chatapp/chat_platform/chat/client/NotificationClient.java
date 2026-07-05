package chatapp.chat_platform.chat.client;

import chatapp.chat_platform.notification.dto.NotificationRequest;
import chatapp.chat_platform.notification.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

/**
 * Internal client used by the Chat/Room API to call the Notification API.
 * Satisfies the inter-module communication requirement from the project spec:
 * "When a new message is sent, the Chat/Room API calls the Notification API
 *  to create a notification for relevant users."
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationClient {

    private final RestTemplate restTemplate;

    @Value("${server.servlet.context-path:/api}")
    private String contextPath;

    @Value("${server.port:8080}")
    private String serverPort;

    private String getBaseUrl() {
        return "http://localhost:" + serverPort + contextPath;
    }

    /**
     * Notifies all room members (except the sender) that a new message arrived.
     *
     * @param roomId    the room the message was sent in
     * @param roomName  name of the room (used in notification message text)
     * @param messageId the saved message's ID
     * @param senderId  the user who sent the message — excluded from notifications
     * @param memberIds all members of the room
     */
    public void notifyRoomMembers(Long roomId,
                                   String roomName,
                                   Long messageId,
                                   Long senderId,
                                   Set<Long> memberIds) {
        String url = getBaseUrl() + "/notifications";

        for (Long memberId : memberIds) {
            if (memberId.equals(senderId)) {
                continue; // don't notify the sender themselves
            }
            try {
                NotificationRequest request = new NotificationRequest();
                request.setUserId(memberId);
                request.setType("NEW_MESSAGE");
                request.setMessage("New message in room: " + roomName);
                request.setRelatedRoomId(roomId);
                request.setRelatedMessageId(messageId);

                restTemplate.postForObject(url, request, Notification.class);
                log.debug("Notification sent to user {} for message {} in room {}", memberId, messageId, roomId);

            } catch (Exception e) {
                // Log but do not fail the message send if notification delivery fails
                log.error("Failed to send notification to user {}: {}", memberId, e.getMessage());
            }
        }
    }

    /**
     * Notifies a specific user that they have been added to a room.
     *
     * @param userId   the user who joined
     * @param roomId   the room they joined
     * @param roomName name of the room
     */
    public void notifyUserJoinedRoom(Long userId, Long roomId, String roomName) {
        String url = getBaseUrl() + "/notifications";
        try {
            NotificationRequest request = new NotificationRequest();
            request.setUserId(userId);
            request.setType("ROOM_JOINED");
            request.setMessage("You have joined room: " + roomName);
            request.setRelatedRoomId(roomId);

            restTemplate.postForObject(url, request, Notification.class);
            log.debug("Room-joined notification sent to user {} for room {}", userId, roomId);

        } catch (Exception e) {
            log.error("Failed to send room-joined notification to user {}: {}", userId, e.getMessage());
        }
    }
}
