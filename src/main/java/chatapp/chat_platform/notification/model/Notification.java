package chatapp.chat_platform.notification.model;

import chatapp.chat_platform.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {

    @Column(nullable = false)
    private Long userId;

    /**
     * Notification type — stored as the enum name string in the DB.
     * Backward compatible with existing String-based entries.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    @Builder.Default
    private boolean read = false;

    private Long relatedRoomId;
    private Long relatedMessageId;
}
