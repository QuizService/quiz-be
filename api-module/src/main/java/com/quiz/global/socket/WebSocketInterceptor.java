package com.quiz.global.socket;

import com.quiz.global.exception.auth.AuthException;
import com.quiz.global.security.jwt.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import static com.quiz.global.exception.auth.AuthErrorCode.JWT_NOT_VALID;

@Component
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {
    private final JwtTokenizer jwtTokenizer;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        assert accessor != null;
        if(accessor.getCommand() == StompCommand.CONNECT) {
            String jwt = accessor.getFirstNativeHeader("Authorization");

            if(jwt == null || !jwtTokenizer.isTokenValid(jwt)) {
                throw new AuthException(JWT_NOT_VALID);
            }
        }
        return message;
    }
}
