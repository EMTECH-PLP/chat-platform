package chatapp.chat_platform.auth.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @Size(min = 3, max = 50)
    private String username;

    @Size(max = 50)
    private String displayName;

    @Size(max = 200)
    private String bio;

    @Size(max = 255)
    private String avatarUrl;
}
