package chatapp.chat_platform.chat.dto.response;

import chatapp.chat_platform.chat.model.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RoomMemberResponse {
    private Long id;
    private Long userId;
    private Role role;
    private LocalDateTime joinedAt;
    private boolean notificationsEnabled;
}