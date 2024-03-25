package com.quiz.global.scheduler;

import org.springframework.stereotype.Component;

@Component
public class ParticipantScheduler {
    /*
    * 대기열 로직
    *
    * 웹소켓으로 본인 순서 주기적으로 확인
    * 만약 queue에서 빠져 나가면 저장 후 api response 전송
    *
    * front-end에서 페이지 이동
    * */
}
