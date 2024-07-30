package com.quiz.global.event;


import com.quiz.domain.participantsinfo.service.ParticipantInfoFacade;
import com.quiz.domain.response.dto.ResponsesSaveDto;
import com.quiz.domain.response.service.ResponsesFacade;
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
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void saveResponses(ResponsesSaveDto dto) {
        try {
            log.info("start save response");
            responsesFacade.calculateScoreAndSaveResponse(dto.getQuizId(), dto.getResponses(), dto.getParticipantInfoId());
        } catch (Exception e) {
            throw e;
        }
    }

    @Async
    @Transactional(value = "mongoTx", propagation = Propagation.REQUIRES_NEW)
//    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendMessage(ParticipantQueueInfoDto participantQueueInfoDto) {
        Long quizId = participantQueueInfoDto.quizId();
        Long userId = participantQueueInfoDto.userId();

        log.info("quizId = {}, userId = {}", quizId, userId);
        String endpoint = String.format("?quiz-id=%d&user-id=%d", quizId, userId);
//        String endpoint = String.format("?quiz-id=%d",quizId);

        messagingTemplate.convertAndSend("/topic/participant" + endpoint, participantQueueInfoDto);
        //참여 가능 시
        if (participantQueueInfoDto.isCapacityLeft()) {
            log.info("save user start");
            participantInfoFacade.saveParticipants(quizId, userId);
        }

    }
}
