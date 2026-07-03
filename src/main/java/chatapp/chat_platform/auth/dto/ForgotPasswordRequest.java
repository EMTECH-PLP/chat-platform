package chatapp.chat_platform.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @NotBlank(message = "Email address cannot be blank")
    @Email(message = "Please provide a valid email address")
    private String email;
}