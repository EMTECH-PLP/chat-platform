package chatapp.chat_platform.search.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

// A full page of search results, plus pagination info
@Value
@Builder
public class SearchResultResponse {

    List<SearchResultItem> results;
    int page;
    int size;
    long totalResults;
    int totalPages;

    // Builds this straight from Spring Data's Page object
    public static SearchResultResponse from(Page<SearchResultItem> resultPage) {
        return SearchResultResponse.builder()
                .results(resultPage.getContent())
                .page(resultPage.getNumber())
                .size(resultPage.getSize())
                .totalResults(resultPage.getTotalElements())
                .totalPages(resultPage.getTotalPages())
                .build();
    }
}