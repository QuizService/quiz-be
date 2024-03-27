package com.quiz.api.participant_info;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ParticipantQueueController {

    @MessageMapping("/queue")
    public void getQueue() {
        log.info("connected");
    }

    @MessageMapping("/test")
    @SendTo("/to/test") // for test
    public String test() {
        log.info("test socket connected");

        return "success";
    }
}
