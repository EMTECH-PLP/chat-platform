package chatapp.chat_platform.search.dto;

import chatapp.chat_platform.search.model.MessageIndex;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;


@Value
@Builder
public class SearchResultItem {

    Long messageId;
    Long roomId;
    Long senderId;
    String content;
    LocalDateTime createdAt;

    // Converts a saved database row into this public-facing shape
    public static SearchResultItem from(MessageIndex entity) {
        return SearchResultItem.builder()
                .messageId(entity.getMessageId())
                .roomId(entity.getRoomId())
                .senderId(entity.getSenderId())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}