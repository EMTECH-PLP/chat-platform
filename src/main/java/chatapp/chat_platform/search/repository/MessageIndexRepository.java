package chatapp.chat_platform.search.repository;

import chatapp.chat_platform.search.model.MessageIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageIndexRepository extends JpaRepository<MessageIndex, Long> {

    @Query("SELECT m FROM MessageIndex m WHERE LOWER(m.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<MessageIndex> searchByContent(@Param("keyword") String keyword);

    @Query("SELECT m FROM MessageIndex m WHERE m.roomId = :roomId AND LOWER(m.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<MessageIndex> searchByContentInRoom(@Param("roomId") Long roomId, @Param("keyword") String keyword);
}




