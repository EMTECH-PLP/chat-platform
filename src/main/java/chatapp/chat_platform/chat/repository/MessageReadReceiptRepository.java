package chatapp.chat_platform.chat.repository;

import chatapp.chat_platform.chat.model.MessageReadReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageReadReceiptRepository extends JpaRepository<MessageReadReceipt, Long> {
}