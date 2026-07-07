package chatapp.chat_platform.chat.client;

import chatapp.chat_platform.chat.dto.request.IndexRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SearchClient {

    private final RestTemplate restTemplate;

    public SearchClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void indexMessage(IndexRequest indexRequest) {
        try {
            restTemplate.postForObject("http://localhost:8080/search/index", indexRequest, Void.class);
        } catch (Exception e) {
            // Log the exception, but don't rethrow it
            System.err.println("Error indexing message: " + e.getMessage());
        }
    }
}