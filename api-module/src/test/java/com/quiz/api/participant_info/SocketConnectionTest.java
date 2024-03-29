package com.quiz.api.participant_info;

import com.quiz.global.socket.WebSocketConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SocketConnectionTest {
    @LocalServerPort
    private int port;
    private WebSocketStompClient stompClient;
    private static final String WEBSOCKET_TOPIC = "/topic/test";
    private static final String WEBSOCKET_DEST = "/app/chat/test";

    CompletableFuture<String> completableFuture;

    @BeforeEach
    void setUp() {
        completableFuture = new CompletableFuture<>();
        stompClient = new WebSocketStompClient(
                new SockJsClient(
                        Collections.singletonList(
                                new WebSocketTransport(new StandardWebSocketClient()))));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }


    @Test
    void socketConnectionTest() throws ExecutionException, InterruptedException, TimeoutException {
        String url = "ws://localhost:" + port +"/web-socket-connection";

        StompSession stompSession  = stompClient.connect(url, new StompSessionHandlerAdapter() {})
                        .get(1, TimeUnit.SECONDS);

        stompSession.subscribe(WEBSOCKET_TOPIC, new DefaultFrameHandler());
        stompSession.send(WEBSOCKET_DEST, "hello");

        String result = completableFuture.get(5, TimeUnit.SECONDS);

        assertThat("success")
                .isEqualTo(result);
    }

    class DefaultFrameHandler implements StompFrameHandler {

        @Override
        public Type getPayloadType(StompHeaders headers) {
            log.info("headers = {}",headers.toString());
            log.info("getSubscription = {}", headers.getSubscription());
            return String.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            completableFuture.complete((String) payload);
        }
    }



}
