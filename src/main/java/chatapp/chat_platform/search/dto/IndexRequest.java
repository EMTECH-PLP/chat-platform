package chatapp.chat_platform.search.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request body received from the Chat API when a new message needs to be indexed.
 */
@Data
public class IndexRequest {

    @NotNull
    private Long messageId;

    @NotNull
    private Long roomId;

    @NotNull
    private Long senderId;

    @NotBlank
    private String content;
}
