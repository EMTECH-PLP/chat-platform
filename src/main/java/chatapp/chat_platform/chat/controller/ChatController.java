package chatapp.chat_platform.chat.controller;

import chatapp.chat_platform.chat.dto.request.CreateRoomRequest;
import chatapp.chat_platform.chat.dto.request.SendMessageRequest;
import chatapp.chat_platform.chat.dto.request.UpdateMessageRequest;
import chatapp.chat_platform.chat.dto.request.UpdateRoomRequest;
import chatapp.chat_platform.chat.dto.response.MessageResponse;
import chatapp.chat_platform.chat.dto.response.RoomResponse;
import chatapp.chat_platform.chat.service.ChatService;
import chatapp.chat_platform.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@Tag(name = "Chat API", description = "APIs for chat rooms and messages")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    @Operation(summary = "Create a new chat room")
    public ApiResponse<RoomResponse> createRoom(@RequestBody CreateRoomRequest request, @RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.ok(chatService.createRoom(request, userId));
    }

    @GetMapping("/{roomId}")
    @Operation(summary = "Get room details")
    public ApiResponse<RoomResponse> getRoom(@PathVariable Long roomId, @RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.ok(chatService.getRoom(roomId, userId));
    }

    @GetMapping
    @Operation(summary = "Get all rooms for the current user")
    public ApiResponse<List<RoomResponse>> getRoomsForUser(@RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.ok(chatService.getRoomsForUser(userId));
    }

    @PutMapping("/{roomId}")
    @Operation(summary = "Update room settings")
    public ApiResponse<RoomResponse> updateRoom(@PathVariable Long roomId, @RequestBody UpdateRoomRequest request, @RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.ok(chatService.updateRoom(roomId, request, userId));
    }

    @DeleteMapping("/{roomId}")
    @Operation(summary = "Delete a room")
    public ApiResponse<Void> deleteRoom(@PathVariable Long roomId, @RequestHeader("X-User-Id") Long userId) {
        chatService.deleteRoom(roomId, userId);
        return ApiResponse.ok(null);
    }

    @PostMapping("/{roomId}/members/{userId}")
    @Operation(summary = "Add a member to a room")
    public ApiResponse<Void> addMember(@PathVariable Long roomId, @PathVariable Long userId, @RequestHeader("X-User-Id") Long currentUserId) {
        chatService.addMember(roomId, userId, currentUserId);
        return ApiResponse.ok(null);
    }

    @DeleteMapping("/{roomId}/members/{userId}")
    @Operation(summary = "Remove a member from a room")
    public ApiResponse<Void> removeMember(@PathVariable Long roomId, @PathVariable Long userId, @RequestHeader("X-User-Id") Long currentUserId) {
        chatService.removeMember(roomId, userId, currentUserId);
        return ApiResponse.ok(null);
    }

    @PostMapping("/messages")
    @Operation(summary = "Send a message to a room")
    public ApiResponse<MessageResponse> sendMessage(@RequestBody SendMessageRequest request, @RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.ok(chatService.sendMessage(request, userId));
    }

    @PutMapping("/messages/{messageId}")
    @Operation(summary = "Update a message")
    public ApiResponse<MessageResponse> updateMessage(@PathVariable Long messageId, @RequestBody UpdateMessageRequest request, @RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.ok(chatService.updateMessage(messageId, request, userId));
    }

    @DeleteMapping("/messages/{messageId}")
    @Operation(summary = "Delete a message")
    public ApiResponse<Void> deleteMessage(@PathVariable Long messageId, @RequestHeader("X-User-Id") Long userId) {
        chatService.deleteMessage(messageId, userId);
        return ApiResponse.ok(null);
    }

    @PostMapping("/messages/{messageId}/reactions/{emoji}")
    @Operation(summary = "Toggle a reaction on a message")
    public ApiResponse<MessageResponse> toggleReaction(@PathVariable Long messageId, @PathVariable String emoji, @RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.ok(chatService.toggleReaction(messageId, emoji, userId));
    }

    @PostMapping("/messages/{messageId}/pin")
    @Operation(summary = "Pin a message in a room")
    public ApiResponse<MessageResponse> pinMessage(@PathVariable Long messageId, @RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.ok(chatService.pinMessage(messageId, userId));
    }

    @GetMapping("/{roomId}/messages")
    @Operation(summary = "Get paginated messages for a room")
    public ApiResponse<Page<MessageResponse>> getMessages(@PathVariable Long roomId, @RequestHeader("X-User-Id") Long userId, @PageableDefault(size = 50) Pageable pageable) {
        return ApiResponse.ok(chatService.getMessages(roomId, userId, pageable));
    }

    @GetMapping("/{roomId}/messages/pinned")
    @Operation(summary = "Get the pinned message for a room")
    public ApiResponse<MessageResponse> getPinnedMessage(@PathVariable Long roomId, @RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.ok(chatService.getPinnedMessage(roomId, userId));
    }

    @PostMapping("/messages/{messageId}/read")
    @Operation(summary = "Mark a message as read")
    public ApiResponse<Void> markAsRead(@PathVariable Long messageId, @RequestHeader("X-User-Id") Long userId) {
        chatService.markAsRead(messageId, userId);
        return ApiResponse.ok(null);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for public rooms")
    public ApiResponse<Page<RoomResponse>> searchPublicRooms(@RequestParam String q, @PageableDefault(size = 50) Pageable pageable) {
        return ApiResponse.ok(chatService.searchPublicRooms(q, pageable));
    }
}