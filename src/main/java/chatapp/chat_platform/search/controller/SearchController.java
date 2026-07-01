package chatapp.chat_platform.search.controller;

import chatapp.chat_platform.common.response.ApiResponse;
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

    @PostMapping
    public ResponseEntity<ApiResponse<?>> search(@Valid @RequestBody SearchRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(searchService.search(request)));
    }
}
