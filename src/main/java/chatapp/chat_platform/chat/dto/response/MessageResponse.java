package chatapp.chat_platform.chat.dto.response;

import chatapp.chat_platform.chat.model.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
public class MessageResponse {
    private Long id;
    private Long roomId;
    private Long senderId;
    private MessageType type;
    private String content;
    private String mediaUrl;
    private String fileName;
    private String mimeType;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
    private boolean pinned;
    private Map<String, Set<Long>> reactions;
    private MessageResponse replyTo;
}