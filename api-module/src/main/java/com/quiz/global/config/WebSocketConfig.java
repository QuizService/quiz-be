package com.quiz.global.config;


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
        registry.addEndpoint("/web-socket-connection") //web socket connection이 최초로 이루어지는 곳
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/to"); //메모리 기반 메시지 브로커가 /to 가 붙은 클라이언트에게 메시지 전달을 할 수 있도록 활성화 (to)
        registry.setApplicationDestinationPrefixes("/from"); //@MessageMapping 이 붙은 곳에 prefix 추가 (from)
    }
}
