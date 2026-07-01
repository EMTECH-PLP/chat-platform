package chatapp.chat_platform.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageRequest {

    @NotNull
    private Long senderId;

    @NotBlank
    private String content;
}
