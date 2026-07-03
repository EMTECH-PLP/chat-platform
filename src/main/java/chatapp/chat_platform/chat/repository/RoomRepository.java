package chatapp.chat_platform.chat.repository;

import chatapp.chat_platform.chat.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByMemberIdsContaining(UUID userId);
    boolean existsByName(String name);
}
