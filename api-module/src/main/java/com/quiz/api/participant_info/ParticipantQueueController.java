package com.quiz.api.participant_info;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ParticipantQueueController {
    @MessageMapping("/queue")
    public void getQueue() {
        log.info("connected");
    }

    @MessageMapping("/chat/test") // 여기로 메시지 발행
    @SendTo("/topic/test") // 이 url을 구독한 유저에게 메시지 전달
    public String test(String params) {
        log.info("test socket connected, params = {}", params);

        return "success";
    }
}
