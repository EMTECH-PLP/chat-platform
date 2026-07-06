package chatapp.chat_platform.chat.repository;

import chatapp.chat_platform.chat.model.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
    Optional<RoomMember> findByRoomIdAndUserId(Long roomId, Long userId);
    Set<RoomMember> findByRoomId(Long roomId);
    List<RoomMember> findByUserId(Long userId);
}