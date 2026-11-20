package chatapp.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        String channel = new String(message.getChannel());
        String payload = new String(message.getBody());

        // Extract room ID from channel
        // chatroom:general:channel
        String roomId = channel.split(":")[1];

        // Broadcast to WebSocket subscribers
        messagingTemplate.convertAndSend(
                "/topic/chatrooms/" + roomId,
                payload
        );
    }
}
