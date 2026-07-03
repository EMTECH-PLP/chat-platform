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
    public ResponseEntity<ApiResponse<?>> countUnread(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok(notificationService.countUnreadForUser(userId)));
    }

    @PatchMapping("/user/{userId}/{id}/read")
    public ResponseEntity<ApiResponse<?>> markRead(@PathVariable Long userId, @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Marked as read", notificationService.markRead(userId, id)));
    }

    @PatchMapping("/user/{userId}/read-all")
    public ResponseEntity<ApiResponse<?>> markAllRead(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok("Marked all as read", notificationService.markAllRead(userId)));
    }

    @DeleteMapping("/user/{userId}/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long userId, @PathVariable Long id) {
        notificationService.delete(userId, id);
        return ResponseEntity.ok(ApiResponse.ok("Notification deleted", null));
    }
}
