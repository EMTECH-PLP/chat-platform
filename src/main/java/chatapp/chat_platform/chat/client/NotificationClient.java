package chatapp.chat_platform.chat.client;

import chatapp.chat_platform.chat.dto.request.NotificationRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotificationClient {

    private final RestTemplate restTemplate;

    public NotificationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendNotification(NotificationRequest notificationRequest) {
        try {
            restTemplate.postForObject("http://localhost:8080/notifications", notificationRequest, Void.class);
        } catch (Exception e) {
            // Log the exception, but don't rethrow it
            System.err.println("Error sending notification: " + e.getMessage());
        }
    }
}