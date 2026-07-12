package chatapp.chat_platform.notification.controller;

import chatapp.chat_platform.common.response.ApiResponse;
import chatapp.chat_platform.notification.dto.NotificationRequest;
import chatapp.chat_platform.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@Valid @RequestBody NotificationRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Notification created", notificationService.create(request)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<?>> getForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok(notificationService.getForUser(userId)));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<ApiResponse<?>> getUnread(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok(notificationService.getUnreadForUser(userId)));
    }

    @GetMapping("/user/{userId}/unread/count")
    public ResponseEntity<ApiResponse<?>> getUnreadCount(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok(notificationService.getUnreadCount(userId)));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<?>> markRead(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Marked as read", notificationService.markRead(id)));
    }

    @PatchMapping("/user/{userId}/read-all")
    public ResponseEntity<ApiResponse<?>> markAllRead(@PathVariable Long userId) {
        notificationService.markAllReadForUser(userId);
        return ResponseEntity.ok(ApiResponse.ok("All notifications marked as read", null));
    }
}
