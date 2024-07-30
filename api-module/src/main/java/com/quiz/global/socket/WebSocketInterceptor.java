package com.quiz.global.socket;

import com.quiz.exception.AuthException;
import com.quiz.global.security.jwt.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import static com.quiz.exception.code.AuthErrorCode.JWT_NOT_VALID;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {
    private final JwtTokenizer jwtTokenizer;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("websocket interceptor start");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (accessor.getCommand() == StompCommand.CONNECT) {
            String jwt = accessor.getFirstNativeHeader("Authorization");

            if (jwt == null || !jwtTokenizer.isTokenValid(jwt)) {
                throw new AuthException(JWT_NOT_VALID);
            }
        }
        return message;
    }
}
