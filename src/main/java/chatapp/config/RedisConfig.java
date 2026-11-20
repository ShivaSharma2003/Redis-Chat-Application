package chatapp.config;

import chatapp.service.RedisMessageSubscriber;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.PatternTopic;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisMessageSubscriber subscriber;

    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory
    ) {

        RedisMessageListenerContainer container =
                new RedisMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);

        container.addMessageListener(
                subscriber,
                new PatternTopic("chatroom:*:channel")
        );

        return container;
    }
}
