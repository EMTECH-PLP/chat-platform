package chatapp.chat_platform.notification.controller;

import chatapp.chat_platform.common.response.ApiResponse;
import chatapp.chat_platform.notification.dto.NotificationRequest;
import chatapp.chat_platform.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

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
    // TODO: once JWT auth exists, remove {userId} here and derive it from
    // the authenticated user instead, per the design doc's security section.
    @GetMapping("/user/{userId}/unreadcount")
    public ResponseEntity<ApiResponse<?>> getUnreadCount(@PathVariable Long userId) {
        long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(ApiResponse.ok(Map.of("unreadCount", count)));
    }


    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<?>> markRead(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Marked as read", notificationService.markRead(id)));
    }
    // TODO: once JWT auth exists, remove {userId} here and derive it from
    // the authenticated user instead, per the design doc's security section.
    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<ApiResponse<?>> markAllAsRead(@PathVariable Long userId) {
        int updated = notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(ApiResponse.ok(Map.of("updated", updated)));
    }

}
