package chatapp.repository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RedisChatRepository {

    private final StringRedisTemplate redisTemplate;

    // 1. Store room metadata using HASH
    public void createRoom(Map<String, String> metadata) {

        redisTemplate.opsForHash()
                .putAll("chatroom:" + metadata.get("roomId"), metadata);
    }

    // 2. Check if room exists
    public boolean roomExists(String roomId) {

        return Boolean.TRUE.equals(
                redisTemplate.hasKey("chatroom:" + roomId)
        );
    }

    // 3. Add participant using SET
    public void addParticipant(String roomId, String participant) {

        redisTemplate.opsForSet()
                .add("chatroom:" + roomId + ":participants", participant);
    }

    // 4. Check participant membership
    public boolean isParticipant(String roomId, String participant) {

        return Boolean.TRUE.equals(
                redisTemplate.opsForSet().isMember(
                        "chatroom:" + roomId + ":participants",
                        participant
                )
        );
    }

    // 5. Store message using LIST
    public void saveMessage(String roomId, String messageJson) {

        redisTemplate.opsForList()
                .rightPush("chatroom:" + roomId + ":messages", messageJson);
    }

    // 6. Retrieve last N messages
    public List<String> getMessages(String roomId, int limit) {

        return redisTemplate.opsForList()
                .range("chatroom:" + roomId + ":messages", -limit, -1);
    }

    // 7. Retrieve participants
    public Set<String> getParticipants(String roomId) {

        return redisTemplate.opsForSet()
                .members("chatroom:" + roomId + ":participants");
    }

    // 8. Retrieve room metadata
    public Map<Object, Object> getRoomMetadata(String roomId) {

        return redisTemplate.opsForHash()
                .entries("chatroom:" + roomId);
    }

    public void deleteRoom(String roomId) {

        String roomKey = "chatroom:" + roomId;

        String participantsKey =
                roomKey + ":participants";

        String messagesKey =
                roomKey + ":messages";

        redisTemplate.delete(List.of(
                roomKey,
                participantsKey,
                messagesKey
        ));
    }
}
