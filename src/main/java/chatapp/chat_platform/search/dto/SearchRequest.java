package chatapp.chat_platform.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// The shape someone must send when they want to search messages.
// Only "keyword" is required; roomId/senderId/page/size are optional filters.
@Data
public class SearchRequest {

    @NotBlank(message = "keyword must not be blank")
    @Size(min = 2, max = 200, message = "keyword must be between 2 and 200 characters")
    private String keyword; // min 2 chars so a 1-letter search can't scan everything

    private Long roomId;   // optional: only search within one chat
    private Long senderId; // optional: only search messages from one person

    @Min(value = 0, message = "page must not be negative")
    @Schema(defaultValue = "0")
    private int page = 0;

    @Min(value = 1, message = "size must be at least 1")
    @Max(value = 100, message = "size must not exceed 100")
    @Schema(defaultValue = "20")
    private int size = 20; // capped so nobody can request the whole table at once

    // Trims whitespace so " hello " and "hello" behave the same
    public String getKeyword() {
        return keyword == null ? null : keyword.trim();
    }
}