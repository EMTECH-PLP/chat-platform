package chatapp.chat_platform.chat.service;

import chatapp.chat_platform.chat.client.NotificationClient;
import chatapp.chat_platform.chat.client.SearchClient;
import chatapp.chat_platform.chat.dto.MessageRequest;
import chatapp.chat_platform.chat.dto.RoomRequest;
import chatapp.chat_platform.chat.model.Message;
import chatapp.chat_platform.chat.model.Room;
import chatapp.chat_platform.chat.repository.MessageRepository;
import chatapp.chat_platform.chat.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final NotificationClient notificationClient;
    private final SearchClient searchClient;

    public Room createRoom(RoomRequest request) {
        if (roomRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Room name already exists");
        }
        Room room = Room.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdByUserId(request.getCreatedByUserId())
                .build();
        room.getMemberIds().add(request.getCreatedByUserId());
        return roomRepository.save(room);
    }

    public Room joinRoom(Long roomId, Long userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        room.getMemberIds().add(userId);
        Room saved = roomRepository.save(room);

        // Notify the user who just joined via the Notification API
        notificationClient.notifyUserJoinedRoom(userId, roomId, room.getName());

        return saved;
    }

    public Message sendMessage(Long roomId, MessageRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        Message message = Message.builder()
                .roomId(roomId)
                .senderId(request.getSenderId())
                .content(request.getContent())
                .build();
        Message saved = messageRepository.save(message);

        // 1. Call Notification API — notify all room members except the sender
        notificationClient.notifyRoomMembers(
                roomId,
                room.getName(),
                saved.getId(),
                request.getSenderId(),
                room.getMemberIds()
        );

        // 2. Call Search API — index this message so it appears in future searches
        searchClient.indexMessage(
                saved.getId(),
                roomId,
                request.getSenderId(),
                request.getContent()
        );

        log.debug("Message {} sent in room {} by user {}", saved.getId(), roomId, request.getSenderId());
        return saved;
    }

    public List<Message> getRoomMessages(Long roomId) {
        return messageRepository.findByRoomIdOrderByCreatedAtAsc(roomId);
    }

    public List<Room> getUserRooms(Long userId) {
        return roomRepository.findByMemberIdsContaining(userId);
    }
}
