package chatapp.chat_platform.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login request")
public class LoginRequest {

    @Schema(description = "Username", example = "alice")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "Password", example = "SecurePass123!")
    @NotBlank(message = "Password is required")
    private String password;
}