package chatapp.chat_platform.search.controller;

import chatapp.chat_platform.common.response.ApiResponse;
import chatapp.chat_platform.search.dto.IndexRequest;
import chatapp.chat_platform.search.dto.SearchRequest;
import chatapp.chat_platform.search.service.SearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    /**
     * Search messages by keyword, optionally filtered to a specific room.
     * Called by clients directly.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> search(@Valid @RequestBody SearchRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(searchService.search(request)));
    }

    /**
     * Index a new message so it is searchable.
     * Called internally by the Chat/Room API after a message is saved.
     */
    @PostMapping("/index")
    public ResponseEntity<ApiResponse<?>> index(@Valid @RequestBody IndexRequest request) {
        searchService.index(
                request.getMessageId(),
                request.getRoomId(),
                request.getSenderId(),
                request.getContent()
        );
        return ResponseEntity.ok(ApiResponse.ok("Message indexed successfully"));
    }
}
