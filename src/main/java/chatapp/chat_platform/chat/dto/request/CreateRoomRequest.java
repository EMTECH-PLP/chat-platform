package chatapp.chat_platform.chat.dto.request;

import chatapp.chat_platform.chat.model.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class CreateRoomRequest {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private RoomType roomType;
    private Set<Long> memberIds;
}