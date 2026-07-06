package chatapp.chat_platform.chat.model;

import chatapp.chat_platform.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @Column(nullable = false)
    private Long createdByUserId;

    private LocalDateTime createdAt;

    private Long updatedByUserId;

    private LocalDateTime deletedAt;

    private Long deletedByUserId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pinned_message_id")
    private Message pinnedMessage;

    private Long pinnedByUserId;

    private LocalDateTime pinnedAt;

    @Builder.Default
    private boolean readOnly = false;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<RoomMember> members = new HashSet<>();
}