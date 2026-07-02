package chatapp.chat_platform.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class GetProfileRequest {

    @NotNull
    private UUID userId;
}
