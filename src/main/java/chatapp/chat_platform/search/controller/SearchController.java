package chatapp.chat_platform.search.controller;

import chatapp.chat_platform.common.response.ApiResponse;
import chatapp.chat_platform.search.dto.SearchRequest;
import chatapp.chat_platform.search.dto.SearchResultResponse;
import chatapp.chat_platform.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Only one door now: searching. Indexing happens automatically via the
// event listener, not through a request anyone sends here.
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "Search chat messages by keyword")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "Search messages")
    @PostMapping
    public ResponseEntity<ApiResponse<SearchResultResponse>> search(@Valid @RequestBody SearchRequest request) {
        SearchResultResponse result = searchService.search(request);
        return ResponseEntity.ok(ApiResponse.ok("Search completed", result));
    }
}