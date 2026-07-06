package chatapp.chat_platform.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateMessageRequest {
    @NotBlank
    private String content;
}