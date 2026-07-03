package chatapp.chat_platform.notification.controller;

import chatapp.chat_platform.auth.util.AuthConstants;
import chatapp.chat_platform.common.response.ApiResponse;
import chatapp.chat_platform.notification.dto.NotificationRequest;
import chatapp.chat_platform.notification.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@Valid @RequestBody NotificationRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Notification created", notificationService.create(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getForUser(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute(AuthConstants.USER_ID_ATTRIBUTE);
        return ResponseEntity.ok(ApiResponse.ok(notificationService.getForUser(userId)));
    }

    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<?>> getUnread(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute(AuthConstants.USER_ID_ATTRIBUTE);
        return ResponseEntity.ok(ApiResponse.ok(notificationService.getUnreadForUser(userId)));
    }

    @GetMapping("/unread/count")
    public ResponseEntity<ApiResponse<?>> getUnreadCount(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute(AuthConstants.USER_ID_ATTRIBUTE);
        long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(ApiResponse.ok(Map.of("unreadCount", count)));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<?>> markRead(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok("Marked as read", notificationService.markRead(id)));
    }

    @PatchMapping("/{id}/dismiss")
    public ResponseEntity<ApiResponse<?>> dismiss(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok("Notification dismissed", notificationService.dismiss(id)));
    }

    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<?>> markAllAsRead(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute(AuthConstants.USER_ID_ATTRIBUTE);
        int updated = notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(ApiResponse.ok(Map.of("updated", updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable UUID id) {
        notificationService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Notification deleted", null));
    }
}
