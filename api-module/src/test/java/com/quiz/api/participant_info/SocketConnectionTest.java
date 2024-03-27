package com.quiz.api.participant_info;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SocketConnectionTest {
    @LocalServerPort
    private int port;

    @Test
    void socketConnectionTest() throws ExecutionException, InterruptedException, TimeoutException {
        WebSocketStompClient stompClient =
                new WebSocketStompClient(
                        new SockJsClient(List.of(
                                new WebSocketTransport(
                                        new StandardWebSocketClient()
                                )
                        ))
                );

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        String url = String.format("ws://localhost:%d/from/test", port);
        StompSession stompSession = stompClient.connectAsync(
                url, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);
        stompSession.subscribe("/to/test", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return null;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {

            }
        })
    }

}
