package com.lecturefeed.socket;

import com.lecturefeed.socket.controller.core.CustomHandshakeHandler;
import com.lecturefeed.socket.controller.core.event.WebSocketSessionEventPublisher;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;


@AllArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final CustomHandshakeHandler customHandshakeHandler;
    private final WebSocketSessionEventPublisher webSocketSessionEventPublisher;


    @Override
    public void configureWebSocketTransport(final WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(final WebSocketHandler handler) {
                return new WebSocketHandlerDecorator(handler) {
                    @Override
                    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
                        webSocketSessionEventPublisher.onWebSocketSessionCreated(session);
                        super.afterConnectionEstablished(session);
                    }
                };
            }
        });
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/admin", "/participant", "/session");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/api/ws")
                .setAllowedOriginPatterns("http://localhost:4200")
                .setHandshakeHandler(customHandshakeHandler)
                .withSockJS();
    }



}