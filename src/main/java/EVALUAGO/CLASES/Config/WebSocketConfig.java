package EVALUAGO.CLASES.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Permite la conexión desde el frontend
        registry.addEndpoint("/clase-websocket").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // /topic para grupos, /queue para mensajes privados (WebRTC)
        registry.enableSimpleBroker("/topic", "/queue");
        // Prefijo para que los mensajes lleguen a los @MessageMapping
        registry.setApplicationDestinationPrefixes("/app");
        // Prefijo necesario para convertAndSendToUser
        registry.setUserDestinationPrefix("/user");
    }
}