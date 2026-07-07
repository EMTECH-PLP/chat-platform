package chatapp.chat_platform.chat.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateRoomRequest {
    private String name;
    private String description;
    private Boolean readOnly;
}