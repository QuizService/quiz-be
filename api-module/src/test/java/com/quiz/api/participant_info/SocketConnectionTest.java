//package com.quiz.api.participant_info;
//
//import com.quiz.global.security.mockuser.WithMockCustomUser;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.messaging.converter.MappingJackson2MessageConverter;
//import org.springframework.messaging.simp.stomp.*;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.web.socket.client.standard.StandardWebSocketClient;
//import org.springframework.web.socket.messaging.WebSocketStompClient;
//import org.springframework.web.socket.sockjs.client.SockJsClient;
//import org.springframework.web.socket.sockjs.client.WebSocketTransport;
//
//import java.lang.reflect.Type;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeoutException;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//public class SocketConnectionTest {
//    @LocalServerPort
//    private int port;
//
//    private WebSocketStompClient stompClient;
//
//    private StompSession stompSession;
//
//    @Autowired
//    private ParticipantQueueController participantQueueController;
//
//    @BeforeEach
//    void setUp() throws ExecutionException, InterruptedException, TimeoutException {
//        stompClient = new WebSocketStompClient(
//                new SockJsClient(List.of(
//                        new WebSocketTransport(
//                                new StandardWebSocketClient()
//                        )
//                ))
//        );
//        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
//        String url = String.format("ws://localhost:%d/web-socket-connection", port);
//        stompSession = stompClient.connectAsync(
//                        url, new TestSessionHandler())
//                .get(10, TimeUnit.SECONDS);
//    }
//
//    @WithMockCustomUser
//    @Test
//    void socketConnectionTest() throws ExecutionException, InterruptedException, TimeoutException {
//        CompletableFuture<String> subscribeFuture = new CompletableFuture<>();
//        stompSession.subscribe("/from/test", new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return String.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//
//                subscribeFuture.complete((String)payload);
//            }
//        });
//        stompSession.send("/to/test","");
//
//        String result = subscribeFuture.get(3, TimeUnit.SECONDS);
//
//        assertThat(result).isEqualTo("success");
//    }
//
//    private class TestSessionHandler extends StompSessionHandlerAdapter {
//        @Override
//        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
//            exception.printStackTrace();
//        }
//    }
//
//    private class TestStompFrameHandler implements StompFrameHandler {
//        @Override
//        public Type getPayloadType(StompHeaders headers) {
//            return String.class;
//        }
//
//        @Override
//        public void handleFrame(StompHeaders headers, Object payload) {
//            System.out.println("Received message: " + payload);
//            // Process your received message here
//        }
//    }
//
//}
