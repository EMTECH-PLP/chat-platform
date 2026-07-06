package chatapp.chat_platform.chat.dto.request;

import chatapp.chat_platform.chat.model.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SendMessageRequest {
    @NotNull
    private Long roomId;
    @NotNull
    private Long senderId;
    @NotNull
    private MessageType type;
    @NotBlank
    private String content;
    private String mediaUrl;
    private String fileName;
    private String mimeType;
    private Long replyToMessageId;
}