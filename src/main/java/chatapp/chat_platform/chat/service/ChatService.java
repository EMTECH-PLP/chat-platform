package chatapp.chat_platform.chat.service;

import chatapp.chat_platform.chat.dto.MessageRequest;
import chatapp.chat_platform.chat.dto.RoomRequest;
import chatapp.chat_platform.chat.model.Message;
import chatapp.chat_platform.chat.model.Room;
import chatapp.chat_platform.chat.repository.MessageRepository;
import chatapp.chat_platform.chat.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;

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

    public Room joinRoom(Long roomId, UUID userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        room.getMemberIds().add(userId);
        return roomRepository.save(room);
    }

    public Message sendMessage(Long roomId, MessageRequest request) {
        roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        Message message = Message.builder()
                .roomId(roomId)
                .senderId(request.getSenderId())
                .content(request.getContent())
                .build();
        return messageRepository.save(message);
        // TODO: notify Notification API and Search API
    }

    public List<Message> getRoomMessages(Long roomId) {
        return messageRepository.findByRoomIdOrderByCreatedAtAsc(roomId);
    }

    public List<Room> getUserRooms(UUID userId) {
        return roomRepository.findByMemberIdsContaining(userId);
    }
}
