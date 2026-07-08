package chatapp.chat_platform.search.repository;

import chatapp.chat_platform.search.model.MessageIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// Talks directly to the database. One flexible query handles all filter
// combinations instead of writing a separate method per combination.
public interface MessageIndexRepository extends JpaRepository<MessageIndex, Long> {

    @Query("""
            SELECT m FROM MessageIndex m
            WHERE LOWER(m.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
              AND (:roomId IS NULL OR m.roomId = :roomId)
              AND (:senderId IS NULL OR m.senderId = :senderId)
            """)
    Page<MessageIndex> search(
            @Param("keyword") String keyword,
            @Param("roomId") Long roomId,
            @Param("senderId") Long senderId,
            Pageable pageable
    );

    boolean existsByMessageId(Long messageId); // used to avoid saving the same message twice

    void deleteByMessageId(Long messageId); // for future use: message edited/deleted upstream
}