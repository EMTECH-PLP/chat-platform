package chatapp.chat_platform.chat.controller;

import chatapp.chat_platform.chat.dto.MessageRequest;
import chatapp.chat_platform.chat.dto.RoomRequest;
import chatapp.chat_platform.chat.service.ChatService;
import chatapp.chat_platform.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createRoom(@Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Room created", chatService.createRoom(request)));
    }

    @PostMapping("/{roomId}/join/{userId}")
    public ResponseEntity<ApiResponse<?>> joinRoom(@PathVariable Long roomId, @PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.ok("Joined room", chatService.joinRoom(roomId, userId)));
    }

    @PostMapping("/{roomId}/messages")
    public ResponseEntity<ApiResponse<?>> sendMessage(@PathVariable Long roomId,
                                                       @Valid @RequestBody MessageRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Message sent", chatService.sendMessage(roomId, request)));
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<ApiResponse<?>> getMessages(@PathVariable Long roomId) {
        return ResponseEntity.ok(ApiResponse.ok(chatService.getRoomMessages(roomId)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<?>> getUserRooms(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.ok(chatService.getUserRooms(userId)));
    }
}
