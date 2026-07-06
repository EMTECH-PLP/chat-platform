package chatapp.chat_platform.chat.repository;

import chatapp.chat_platform.chat.model.Room;
import chatapp.chat_platform.chat.model.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT r FROM Room r WHERE r.roomType = :roomType AND (LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(r.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Room> searchPublicRooms(String query, RoomType roomType, Pageable pageable);
}