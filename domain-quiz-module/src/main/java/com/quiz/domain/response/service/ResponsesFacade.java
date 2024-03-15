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
    private final ResponsesMongoTemplate responsesMongoTemplate;
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
        //event 활성화
        applicationEventPublisher.publishEvent(params);
    }
}
