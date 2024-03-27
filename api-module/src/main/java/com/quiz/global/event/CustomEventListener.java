package com.quiz.global.event;


import com.quiz.domain.participantsinfo.service.ParticipantInfoFacade;
import com.quiz.domain.response.service.ResponsesFacade;
import com.quiz.domain.response.dto.ResponsesRequestDto;
import com.quiz.global.queue.ParticipantQueueInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomEventListener {
    private final ResponsesFacade responsesFacade;
    private final ParticipantInfoFacade participantInfoFacade;
    private final SimpMessagingTemplate messagingTemplate;


    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void saveResponses(Map<String, Object> params) {
        List<ResponsesRequestDto> responses = (List<ResponsesRequestDto>) params.get("responses");
        String participantInfoId = (String) params.get("participantInfoId");
        Long quizId = (Long) params.get("quizId");

        responsesFacade.calculateScoreAndSaveResponse(quizId, responses, participantInfoId);
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendMessage(ParticipantQueueInfoDto participantQueueInfoDto) {
        Long quizId = participantQueueInfoDto.quizId();
        Long userId = participantQueueInfoDto.userId();
        String endpoint = String.format("?quiz-id=%s&user-id=%s",quizId, userId);

        messagingTemplate.convertAndSend("/" + endpoint, participantQueueInfoDto);
        //참여 가능 시
        if(participantQueueInfoDto.isCapacityLeft()) {
            participantInfoFacade.saveParticipants(quizId, userId);
        }

    }
}
