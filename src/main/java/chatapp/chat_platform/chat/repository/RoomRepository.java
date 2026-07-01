package chatapp.chat_platform.chat.repository;

import chatapp.chat_platform.chat.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByMemberIdsContaining(Long userId);
    boolean existsByName(String name);
}
