package chatapp.chat_platform.chat.model;

import chatapp.chat_platform.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "room_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime joinedAt;

    private LocalDateTime leftAt;

    private LocalDateTime removedAt;

    private Long removedByUserId;

    private String removalReason;

    @Builder.Default
    private boolean notificationsEnabled = true;

    private Long lastReadMessageId;
}