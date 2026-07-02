package chatapp.chat_platform.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Email verification request")
public class VerifyEmailRequest {

    @Schema(description = "Verification token from email", example = "abc123def456...")
    @NotBlank(message = "Token is required")
    private String token;
}