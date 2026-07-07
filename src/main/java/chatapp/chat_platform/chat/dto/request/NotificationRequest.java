package chatapp.chat_platform.chat.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class NotificationRequest {
    private Set<Long> userIds;
    private String type;
    private Object data;
}