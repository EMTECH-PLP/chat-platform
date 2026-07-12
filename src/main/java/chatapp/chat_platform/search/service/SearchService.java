package chatapp.chat_platform.search.service;

import chatapp.chat_platform.search.dto.SearchRequest;
import chatapp.chat_platform.search.dto.SearchResultItem;
import chatapp.chat_platform.search.dto.SearchResultResponse;
import chatapp.chat_platform.search.model.MessageIndex;
import chatapp.chat_platform.search.repository.MessageIndexRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

// The actual logic: saving new messages, and searching saved ones.
// No listeners here — chat calls this indirectly through the controller,
// via a normal HTTP request, not an event.
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final MessageIndexRepository messageIndexRepository;

    // Runs a search and returns a clean, paginated response
    public SearchResultResponse search(SearchRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(Sort.Direction.DESC, "createdAt") // newest first
        );

        Page<MessageIndex> matches = messageIndexRepository.search(
                request.getKeyword(),
                request.getRoomId(),
                request.getSenderId(),
                pageable
        );

        Page<SearchResultItem> dtoPage = matches.map(SearchResultItem::from);
        return SearchResultResponse.from(dtoPage);
    }

    // Saves a new message as searchable. Safe to call twice for the same message.
    public void index(Long messageId, Long roomId, Long senderId, String content) {
        if (messageIndexRepository.existsByMessageId(messageId)) {
            log.debug("Message {} is already indexed, skipping duplicate index request", messageId);
            return;
        }

        MessageIndex entry = MessageIndex.builder()
                .messageId(messageId)
                .roomId(roomId)
                .senderId(senderId)
                .content(content)
                .build();

        messageIndexRepository.save(entry);
        log.debug("Indexed message {} for room {}", messageId, roomId);
    }

    // For future use: removes a message if it's ever deleted upstream
    public void removeFromIndex(Long messageId) {
        messageIndexRepository.deleteByMessageId(messageId);
        log.debug("Removed message {} from the search index", messageId);
    }
}