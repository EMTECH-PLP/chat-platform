package chatapp.chat_platform.chat.dto.response;

import chatapp.chat_platform.chat.model.RoomType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
public class RoomResponse {
    private Long id;
    private String name;
    private String description;
    private RoomType roomType;
    private Long createdByUserId;
    private LocalDateTime createdAt;
    private Long updatedByUserId;
    private boolean readOnly;
    private MessageResponse pinnedMessage;
    private Set<RoomMemberResponse> members;
    private MessageResponse lastMessage;
    private int unreadCount;
}