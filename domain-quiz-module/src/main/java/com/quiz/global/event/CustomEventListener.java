package com.quiz.global.event;


import com.quiz.domain.response.service.ResponsesService;
import com.quiz.dto.responses.ResponsesRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RequiredArgsConstructor
@Component
public class CustomEventListener {
    public ResponsesService responsesService;


    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void saveResponses(Map<String, Object> params) {
        List<ResponsesRequestDto> responses = (List<ResponsesRequestDto>) params.get("responses");
        String participantInfoId = (String) params.get("participantInfoId");

        responsesService.saveResponses(participantInfoId, responses);
    }
}
