package chatapp.chat_platform.chat.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Internal client used by the Chat/Room API to call the Search/History API.
 * Satisfies the inter-module communication requirement from the project spec:
 * "The Chat/Room API makes the new message available so the Search/History API
 *  can index it for future searches."
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SearchClient {

    private final RestTemplate restTemplate;

    @Value("${server.servlet.context-path:/api}")
    private String contextPath;

    @Value("${server.port:8080}")
    private String serverPort;

    private String getBaseUrl() {
        return "http://localhost:" + serverPort + contextPath;
    }

    /**
     * Sends a new message to the Search API to be indexed for future searches.
     *
     * @param messageId ID of the saved message
     * @param roomId    room the message belongs to
     * @param senderId  user who sent the message
     * @param content   text content of the message
     */
    public void indexMessage(Long messageId, Long roomId, Long senderId, String content) {
        String url = getBaseUrl() + "/search/index";
        try {
            Map<String, Object> payload = Map.of(
                    "messageId", messageId,
                    "roomId", roomId,
                    "senderId", senderId,
                    "content", content
            );
            restTemplate.postForObject(url, payload, Void.class);
            log.debug("Message {} indexed for search", messageId);

        } catch (Exception e) {
            // Log but do not fail the message send if indexing fails
            log.error("Failed to index message {} for search: {}", messageId, e.getMessage());
        }
    }
}
