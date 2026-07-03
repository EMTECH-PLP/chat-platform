package chatapp.chat_platform.notification.dto;

import chatapp.chat_platform.notification.model.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class NotificationRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private NotificationType type;

    @NotBlank
    private String title;

    @NotBlank
    private String message;

    private Long relatedRoomId;
    private Long relatedMessageId;
}
