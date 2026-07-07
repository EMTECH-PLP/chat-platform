package chatapp.chat_platform.chat.repository;

import chatapp.chat_platform.chat.model.Message;
import chatapp.chat_platform.chat.model.MessageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.room.id = :roomId AND m.type <> chatapp.chat_platform.chat.model.MessageType.DELETED ORDER BY m.createdAt DESC")
    Page<Message> findMessagesByRoomId(Long roomId, Pageable pageable);

    Optional<Message> findTopByRoomIdAndTypeNotOrderByCreatedAtDesc(Long roomId, MessageType messageType);

    long countByRoomIdAndIdGreaterThanAndTypeNot(Long roomId, Long lastReadMessageId, MessageType messageType);
}