package chatapp.service;

import chatapp.dto.ChatMessage;
import chatapp.exceptions.RoomNotFoundException;
import chatapp.exceptions.UserNotParticipantException;
import chatapp.repository.RedisChatRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final RedisChatRepository repository;
    private final RedisPublisher publisher;
    private final ObjectMapper objectMapper;

    public void sendMessage(
            String roomId,
            String participant,
            String content
    ) {

        // 1. Validate room
        if (!repository.roomExists(roomId)) {
            throw new RoomNotFoundException("Chat room not found");
        }

        // 2. Validate participant
        if (!repository.isParticipant(roomId, participant)) {
            throw new UserNotParticipantException(
                    "User is not a member of this room"
            );
        }

        // 3. Create message
        ChatMessage chatMessage = new ChatMessage(
                participant,
                content,
                Instant.now()
        );

        // 4. Convert Java object to JSON
        String messageJson =
                objectMapper.writeValueAsString(chatMessage);

        // 5. Store message in Redis List
        repository.saveMessage(roomId, messageJson);

        // 6. Publish message to Redis channel
        publisher.publish(
                "chatroom:" + roomId + ":channel",
                messageJson
        );

    }

    public List<ChatMessage> getMessages(
            String roomId,
            int limit
    ) {

        if (!repository.roomExists(roomId)) {
            throw new RoomNotFoundException("Chat room not found");
        }

        if (limit < 1 || limit > 100) {
            throw new IllegalArgumentException(
                    "Limit must be between 1 and 100"
            );
        }

        List<String> messages =
                repository.getMessages(roomId, limit);

        return messages.stream()
                .map(this::deserialize)
                .toList();
    }

    private ChatMessage deserialize(String json) {

        return objectMapper.readValue(
                json, ChatMessage.class
        );
    }
}