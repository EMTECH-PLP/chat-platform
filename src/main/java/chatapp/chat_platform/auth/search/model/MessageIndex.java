package chatapp.chat_platform.auth.search.model;

import chatapp.chat_platform.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "message_index")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageIndex extends BaseEntity {

    @Column(nullable = false)
    private Long messageId;

    @Column(nullable = false)
    private Long roomId;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
}
