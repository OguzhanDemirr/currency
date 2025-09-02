package com.example.currency.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // WS endpoint (SockJS fallback)
    registry.addEndpoint("/ws")
        .setAllowedOriginPatterns(
            "http://localhost:5173",
            "https://currency-ui2.onrender.com",     // kendi UI domainini ekle
            "https://*onrender.com"
        )
        .withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    // /topic ... yayın kanalları (server -> client)
    registry.enableSimpleBroker("/topic");
    // /app ... client'ın server'a mesaj gönderdiği prefix
    registry.setApplicationDestinationPrefixes("/app");
  }
}
