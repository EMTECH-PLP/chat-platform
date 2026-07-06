package chatapp.chat_platform.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminUserRequest {

    @Size(min = 3, max = 50)
    private String username;

    @Email
    @Size(max = 100)
    private String email;

    @Size(max = 50)
    private String displayName;

    @Size(max = 200)
    private String bio;

    @Size(max = 255)
    private String avatarUrl;

    @Size(min = 8, max = 100)
    private String password;
}
