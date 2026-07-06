package chatapp.chat_platform.chat.service;

import chatapp.chat_platform.chat.client.NotificationClient;
import chatapp.chat_platform.chat.client.SearchClient;
import chatapp.chat_platform.chat.dto.ChatMessageMapper;
import chatapp.chat_platform.chat.dto.request.*;
import chatapp.chat_platform.chat.dto.response.MessageResponse;
import chatapp.chat_platform.chat.dto.response.RoomResponse;
import chatapp.chat_platform.chat.model.*;
import chatapp.chat_platform.chat.repository.MessageReadReceiptRepository;
import chatapp.chat_platform.chat.repository.MessageRepository;
import chatapp.chat_platform.chat.repository.RoomMemberRepository;
import chatapp.chat_platform.chat.repository.RoomRepository;
import chatapp.chat_platform.common.exception.ForbiddenException;
import chatapp.chat_platform.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final MessageReadReceiptRepository readReceiptRepository;
    private final NotificationClient notificationClient;
    private final SearchClient searchClient;
    private final ChatMessageMapper chatMessageMapper;

    public RoomResponse createRoom(CreateRoomRequest request, Long creatorId) {
        Room room = Room.builder()
                .name(request.getName())
                .description(request.getDescription())
                .roomType(request.getRoomType())
                .createdByUserId(creatorId)
                .createdAt(LocalDateTime.now())
                .build();

        RoomMember owner = RoomMember.builder()
                .room(room)
                .userId(creatorId)
                .role(Role.OWNER)
                .joinedAt(LocalDateTime.now())
                .build();
        room.getMembers().add(owner);

        if (request.getMemberIds() != null) {
            for (Long memberId : request.getMemberIds()) {
                if (!memberId.equals(creatorId)) {
                    RoomMember member = RoomMember.builder()
                            .room(room)
                            .userId(memberId)
                            .role(Role.MEMBER)
                            .joinedAt(LocalDateTime.now())
                            .build();
                    room.getMembers().add(member);
                }
            }
        }

        Room savedRoom = roomRepository.save(room);
        createSystemMessage(savedRoom, "Room created by user " + creatorId);

        return chatMessageMapper.toRoomResponse(savedRoom, null, 0);
    }

    public RoomResponse getRoom(Long roomId, Long userId) {
        Room room = findRoomOrThrow(roomId);
        RoomMember member = findMemberOrThrow(roomId, userId);
        Message lastMessage = messageRepository.findTopByRoomIdAndTypeNotOrderByCreatedAtDesc(roomId, MessageType.DELETED).orElse(null);
        long unreadCount = messageRepository.countByRoomIdAndIdGreaterThanAndTypeNot(roomId, member.getLastReadMessageId(), MessageType.DELETED);
        return chatMessageMapper.toRoomResponse(room, lastMessage, (int) unreadCount);
    }

    public List<RoomResponse> getRoomsForUser(Long userId) {
        return roomMemberRepository.findByUserId(userId).stream()
                .map(member -> getRoom(member.getRoom().getId(), userId))
                .collect(Collectors.toList());
    }

    public RoomResponse updateRoom(Long roomId, UpdateRoomRequest request, Long userId) {
        Room room = findRoomOrThrow(roomId);
        checkPermission(roomId, userId, Role.ADMIN);

        if (request.getName() != null) {
            room.setName(request.getName());
        }
        if (request.getDescription() != null) {
            room.setDescription(request.getDescription());
        }
        if (request.getReadOnly() != null) {
            room.setReadOnly(request.getReadOnly());
        }
        room.setUpdatedByUserId(userId);

        Room updatedRoom = roomRepository.save(room);
        createSystemMessage(updatedRoom, "Room settings updated by user " + userId);

        return getRoom(roomId, userId);
    }

    public void deleteRoom(Long roomId, Long userId) {
        Room room = findRoomOrThrow(roomId);
        checkPermission(roomId, userId, Role.OWNER);

        room.setDeletedAt(LocalDateTime.now());
        room.setDeletedByUserId(userId);
        roomRepository.save(room);
        createSystemMessage(room, "Room deleted by user " + userId);
    }

    public void addMember(Long roomId, Long userId, Long currentUserId) {
        Room room = findRoomOrThrow(roomId);
        if (room.getRoomType() == RoomType.PRIVATE) {
            checkPermission(roomId, currentUserId, Role.ADMIN);
        }

        if (roomMemberRepository.findByRoomIdAndUserId(roomId, userId).isPresent()) {
            return; // Already a member
        }

        RoomMember member = RoomMember.builder()
                .room(room)
                .userId(userId)
                .role(Role.MEMBER)
                .joinedAt(LocalDateTime.now())
                .build();
        roomMemberRepository.save(member);
        createSystemMessage(room, "User " + userId + " joined the room");
    }

    public void removeMember(Long roomId, Long userId, Long currentUserId) {
        checkPermission(roomId, currentUserId, Role.ADMIN);
        RoomMember member = findMemberOrThrow(roomId, userId);

        member.setLeftAt(LocalDateTime.now());
        member.setRemovedByUserId(currentUserId);
        roomMemberRepository.save(member);

        createSystemMessage(member.getRoom(), "User " + userId + " was removed by user " + currentUserId);
    }

    public MessageResponse sendMessage(SendMessageRequest request, Long senderId) {
        Room room = findRoomOrThrow(request.getRoomId());
        RoomMember sender = findMemberOrThrow(request.getRoomId(), senderId);

        if (room.isReadOnly() && sender.getRole() == Role.MEMBER) {
            throw new ForbiddenException("This room is read-only.");
        }

        Message message = Message.builder()
                .room(room)
                .senderId(senderId)
                .type(request.getType())
                .content(request.getContent())
                .mediaUrl(request.getMediaUrl())
                .fileName(request.getFileName())
                .mimeType(request.getMimeType())
                .createdAt(LocalDateTime.now())
                .build();

        if (request.getReplyToMessageId() != null) {
            message.setReplyTo(findMessageOrThrow(request.getReplyToMessageId()));
        }

        Message savedMessage = messageRepository.save(message);
        markAsRead(savedMessage.getId(), senderId);

        // Fan-out notifications
        Set<Long> recipientIds = room.getMembers().stream()
                .filter(m -> m.isNotificationsEnabled() && !m.getUserId().equals(senderId))
                .map(RoomMember::getUserId)
                .collect(Collectors.toSet());

        notificationClient.sendNotification(NotificationRequest.builder()
                .userIds(recipientIds)
                .type("NEW_MESSAGE")
                .data(chatMessageMapper.toMessageResponse(savedMessage))
                .build());

        // Index for search
        searchClient.indexMessage(IndexRequest.builder()
                .messageId(savedMessage.getId())
                .content(savedMessage.getContent())
                .build());

        return chatMessageMapper.toMessageResponse(savedMessage);
    }

    public MessageResponse updateMessage(Long messageId, UpdateMessageRequest request, Long userId) {
        Message message = findMessageOrThrow(messageId);
        if (!message.getSenderId().equals(userId)) {
            throw new ForbiddenException("You can only edit your own messages.");
        }

        message.setPreviousContent(message.getContent());
        message.setContent(request.getContent());
        message.setEditedAt(LocalDateTime.now());
        message.setEditedByUserId(userId);

        Message updatedMessage = messageRepository.save(message);
        return chatMessageMapper.toMessageResponse(updatedMessage);
    }

    public void deleteMessage(Long messageId, Long userId) {
        Message message = findMessageOrThrow(messageId);
        checkPermission(message.getRoom().getId(), userId, Role.ADMIN);

        message.setContent("This message was deleted.");
        message.setType(MessageType.DELETED);
        message.setDeletedAt(LocalDateTime.now());
        message.setDeletedByUserId(userId);
        messageRepository.save(message);
    }

    public MessageResponse toggleReaction(Long messageId, String emoji, Long userId) {
        Message message = findMessageOrThrow(messageId);
        Set<Long> userIds = message.getReactions().computeIfAbsent(emoji, k -> new HashSet<>());
        if (userIds.contains(userId)) {
            userIds.remove(userId);
        } else {
            userIds.add(userId);
        }
        Message updatedMessage = messageRepository.save(message);
        return chatMessageMapper.toMessageResponse(updatedMessage);
    }

    public MessageResponse pinMessage(Long messageId, Long userId) {
        Message message = findMessageOrThrow(messageId);
        Room room = message.getRoom();
        checkPermission(room.getId(), userId, Role.ADMIN);

        room.setPinnedMessage(message);
        room.setPinnedAt(LocalDateTime.now());
        room.setPinnedByUserId(userId);
        message.setPinned(true);

        messageRepository.save(message);
        roomRepository.save(room);

        createSystemMessage(room, "Message pinned by user " + userId);
        return chatMessageMapper.toMessageResponse(message);
    }

    public Page<MessageResponse> getMessages(Long roomId, Long userId, Pageable pageable) {
        findMemberOrThrow(roomId, userId);
        return messageRepository.findMessagesByRoomId(roomId, pageable)
                .map(chatMessageMapper::toMessageResponse);
    }

    public MessageResponse getPinnedMessage(Long roomId, Long userId) {
        Room room = findRoomOrThrow(roomId);
        findMemberOrThrow(roomId, userId);
        return chatMessageMapper.toMessageResponse(room.getPinnedMessage());
    }

    public void markAsRead(Long messageId, Long userId) {
        Message message = findMessageOrThrow(messageId);
        RoomMember member = findMemberOrThrow(message.getRoom().getId(), userId);

        if (member.getLastReadMessageId() == null || message.getId() > member.getLastReadMessageId()) {
            member.setLastReadMessageId(message.getId());
            roomMemberRepository.save(member);
        }

        MessageReadReceipt receipt = MessageReadReceipt.builder()
                .message(message)
                .member(member)
                .readAt(LocalDateTime.now())
                .build();
        readReceiptRepository.save(receipt);
    }

    public Page<RoomResponse> searchPublicRooms(String query, Pageable pageable) {
        return roomRepository.searchPublicRooms(query, RoomType.PUBLIC, pageable)
                .map(room -> getRoom(room.getId(), 0L)); // 0L for non-member view
    }

    private void createSystemMessage(Room room, String content) {
        Message systemMessage = Message.builder()
                .room(room)
                .senderId(0L) // System message sender ID
                .type(MessageType.SYSTEM)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        messageRepository.save(systemMessage);
    }

    private Room findRoomOrThrow(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("Room not found with id: " + roomId));
    }

    private RoomMember findMemberOrThrow(Long roomId, Long userId) {
        return roomMemberRepository.findByRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new ForbiddenException("User is not a member of this room."));
    }

    private Message findMessageOrThrow(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message not found with id: " + messageId));
    }

    private void checkPermission(Long roomId, Long userId, Role requiredRole) {
        RoomMember member = findMemberOrThrow(roomId, userId);
        boolean isOwner = member.getRole() == Role.OWNER;
        boolean isAdmin = member.getRole() == Role.ADMIN;

        if (requiredRole == Role.OWNER && !isOwner) {
            throw new ForbiddenException("Only the room owner can perform this action.");
        }
        if (requiredRole == Role.ADMIN && !(isOwner || isAdmin)) {
            throw new ForbiddenException("Only room admins or the owner can perform this action.");
        }
    }
}