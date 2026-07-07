package chatapp.chat_platform.chat.dto;

import chatapp.chat_platform.chat.dto.response.MessageResponse;
import chatapp.chat_platform.chat.dto.response.RoomMemberResponse;
import chatapp.chat_platform.chat.dto.response.RoomResponse;
import chatapp.chat_platform.chat.model.Message;
import chatapp.chat_platform.chat.model.Room;
import chatapp.chat_platform.chat.model.RoomMember;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ChatMessageMapper {

    public MessageResponse toMessageResponse(Message message) {
        if (message == null) {
            return null;
        }
        return MessageResponse.builder()
                .id(message.getId())
                .roomId(message.getRoom().getId())
                .senderId(message.getSenderId())
                .type(message.getType())
                .content(message.getContent())
                .mediaUrl(message.getMediaUrl())
                .fileName(message.getFileName())
                .mimeType(message.getMimeType())
                .createdAt(message.getCreatedAt())
                .editedAt(message.getEditedAt())
                .pinned(message.isPinned())
                .reactions(message.getReactions())
                .replyTo(toMessageResponse(message.getReplyTo()))
                .build();
    }

    public RoomMemberResponse toRoomMemberResponse(RoomMember member) {
        if (member == null) {
            return null;
        }
        return RoomMemberResponse.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .role(member.getRole())
                .joinedAt(member.getJoinedAt())
                .notificationsEnabled(member.isNotificationsEnabled())
                .build();
    }

    public RoomResponse toRoomResponse(Room room, Message lastMessage, int unreadCount) {
        if (room == null) {
            return null;
        }
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .description(room.getDescription())
                .roomType(room.getRoomType())
                .createdByUserId(room.getCreatedByUserId())
                .createdAt(room.getCreatedAt())
                .updatedByUserId(room.getUpdatedByUserId())
                .readOnly(room.isReadOnly())
                .pinnedMessage(toMessageResponse(room.getPinnedMessage()))
                .members(room.getMembers().stream().map(this::toRoomMemberResponse).collect(Collectors.toSet()))
                .lastMessage(toMessageResponse(lastMessage))
                .unreadCount(unreadCount)
                .build();
    }
}