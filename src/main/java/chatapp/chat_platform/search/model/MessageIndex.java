package chatapp.chat_platform.search.model;

import chatapp.chat_platform.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "message_index",
        indexes = {
                @Index(name = "idx_message_index_room_id", columnList = "roomId"),
                @Index(name = "idx_message_index_sender_id", columnList = "senderId")
                // messageId is already unique via @Column below, so no extra index needed for it.
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageIndex extends BaseEntity {

    @Column(nullable = false, unique = true)
    private Long messageId; // links back to the original chat message

    @Column(nullable = false)
    private Long roomId; // which chat this belongs to

    @Column(nullable = false)
    private Long senderId; // who sent it

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // the actual searchable text
}