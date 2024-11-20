package com.quiz.global.event;


import com.quiz.domain.participantsinfo.service.ParticipantInfoFacade;
import com.quiz.domain.response.dto.ResponsesSaveDto;
import com.quiz.domain.response.service.ResponsesFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomEventListener {
    private final ResponsesFacade responsesFacade;
    private final ParticipantInfoFacade participantInfoFacade;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void saveResponses(ResponsesSaveDto dto) {
        try {
            responsesFacade.calculateScoreAndSaveResponse(dto.getQuizId(), dto.getResponses(), dto.getParticipantInfoId());
        } catch (Exception e) {
            throw e;
        }
    }
}
