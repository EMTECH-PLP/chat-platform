package chatapp.chat_platform.auth.search.service;

import chatapp.chat_platform.auth.search.dto.SearchRequest;
import chatapp.chat_platform.auth.search.model.MessageIndex;
import chatapp.chat_platform.auth.search.repository.MessageIndexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final MessageIndexRepository messageIndexRepository;

    public List<MessageIndex> search(SearchRequest request) {
        if (request.getRoomId() != null) {
            return messageIndexRepository.searchByContentInRoom(request.getRoomId(), request.getKeyword());
        }
        return messageIndexRepository.searchByContent(request.getKeyword());
    }

    public void index(Long messageId, Long roomId, Long senderId, String content) {
        MessageIndex entry = MessageIndex.builder()
                .messageId(messageId)
                .roomId(roomId)
                .senderId(senderId)
                .content(content)
                .build();
        messageIndexRepository.save(entry);
    }
}
