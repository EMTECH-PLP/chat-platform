package chatapp.chat_platform.chat.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IndexRequest {
    private Long messageId;
    private String content;
}