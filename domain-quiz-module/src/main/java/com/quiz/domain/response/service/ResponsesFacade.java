package com.quiz.domain.response.service;

import com.quiz.domain.participants_info.entity.ParticipantInfo;
import com.quiz.domain.participants_info.service.ParticipantInfoService;
import com.quiz.domain.quiz.service.QuizService;
import com.quiz.domain.response.mongo.ResponsesMongoTemplate;
import com.quiz.dto.responses.ResponsesRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ResponsesFacade {
    private final ParticipantInfoService participantInfoService;
    private final QuizService quizService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void saveResponse(Long quizId, Long userId, List<ResponsesRequestDto> responses) {
        Integer quizCapacity = quizService.findById(quizId)
                        .getCapacity();
        if(quizCapacity > 0) {
            participantInfoService.updateFcFs(quizId, userId, quizCapacity);
        }
        String participantInfoId = participantInfoService.findByQuizIdAndUserId(quizId, userId)
                .getId();

        Map<String, Object> params = new HashMap<>();
        params.put("participantInfoId", participantInfoId);
        params.put("responses", responses);
        //event 로 저장과정 분리
        applicationEventPublisher.publishEvent(params);
    }

    /*
    * 채점 방법
    * 1. 답변 저장 후 바로 실행
    * 2. 답변을 모았다가 한번에 실행
    * 3. 정답을 다른 캐시 저장소에 저장하여 비교
    * */
    public void calculateScore(Long quizId, Long userId) {

    }
}
