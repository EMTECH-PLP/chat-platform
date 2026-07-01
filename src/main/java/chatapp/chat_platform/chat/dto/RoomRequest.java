package chatapp.chat_platform.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Long createdByUserId;
}
