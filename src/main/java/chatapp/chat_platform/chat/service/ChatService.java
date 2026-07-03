package chatapp.chat_platform.chat.service;

import chatapp.chat_platform.chat.dto.MessageRequest;
import chatapp.chat_platform.chat.dto.RoomRequest;
import chatapp.chat_platform.chat.model.Message;
import chatapp.chat_platform.chat.model.Room;
import chatapp.chat_platform.chat.repository.MessageRepository;
import chatapp.chat_platform.chat.repository.RoomRepository;
import chatapp.chat_platform.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final NotificationService notificationService;

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
        return roomRepository.save(room);
    }

    @Transactional
    public Message sendMessage(Long roomId, MessageRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        if (!room.getMemberIds().contains(request.getSenderId())) {
            throw new IllegalArgumentException("Sender is not a member of this room");
        }
        Message message = Message.builder()
                .roomId(roomId)
                .senderId(request.getSenderId())
                .content(request.getContent())
                .build();
        Message savedMessage = messageRepository.save(message);
        notificationService.createMessageNotifications(room, savedMessage);
        return savedMessage;
    }

    public List<Message> getRoomMessages(Long roomId) {
        return messageRepository.findByRoomIdOrderByCreatedAtAsc(roomId);
    }

    public List<Room> getUserRooms(Long userId) {
        return roomRepository.findByMemberIdsContaining(userId);
    }
}
