package chatapp.chat_platform.auth.search.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchRequest {

    @NotBlank
    private String keyword;

    private Long roomId;
}
