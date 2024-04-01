package com.quiz.global.event;


import com.quiz.domain.participantsinfo.service.ParticipantInfoFacade;
import com.quiz.domain.response.dto.ResponsesSaveDto;
import com.quiz.domain.response.service.ResponsesFacade;
import com.quiz.domain.response.dto.ResponsesRequestDto;
import com.quiz.global.queue.ParticipantQueueInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
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
//@Lazy(false)
@RequiredArgsConstructor
@Component
public class CustomEventListener {
    private final ResponsesFacade responsesFacade;
    private final ParticipantInfoFacade participantInfoFacade;
    private final SimpMessagingTemplate messagingTemplate;


    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void saveResponses(ResponsesSaveDto dto) {
        log.info("start save response");

        log.info("save responses");
        responsesFacade.calculateScoreAndSaveResponse(dto.getQuizId(), dto.getResponses(), dto.getParticipantInfoId());
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void sendMessage(ParticipantQueueInfoDto participantQueueInfoDto) {
        Long quizId = participantQueueInfoDto.quizId();
        Long userId = participantQueueInfoDto.userId();

        log.info("quizId = {}, userId = {}", quizId, userId);
        String endpoint = String.format("?quiz-id=%s&user-id=%s",quizId, userId);

        messagingTemplate.convertAndSend("/" + endpoint, participantQueueInfoDto);
        //참여 가능 시
        if(participantQueueInfoDto.isCapacityLeft()) {
            log.info("save user start");
            participantInfoFacade.saveParticipants(quizId, userId);
        }

    }
}
