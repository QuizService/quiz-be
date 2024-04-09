package com.quiz.global.queue;

import com.quiz.domain.participantsinfo.dto.ParticipantQueueDto;
import com.quiz.domain.participantsinfo.repository.redis.ParticipantInfoQueueRepository;
import com.quiz.domain.participantsinfo.service.ParticipantInfoFacade;
import com.quiz.domain.response.service.ResponsesFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@EnableScheduling // 추가
@Component
@RequiredArgsConstructor
public class QueueScheduler {
    private final ParticipantInfoQueueRepository participantInfoQueueRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(value = "redisTx1")
    @Scheduled(fixedDelay = 2000)
    public void queue() {
        Long queueSize = participantInfoQueueRepository.getZSetSize();
        if(queueSize == null) return;
        Set<ParticipantQueueDto> queue = participantInfoQueueRepository.getUsers();
        for (ParticipantQueueDto participantQueueDto : queue) {
            Long quizId = participantQueueDto.quizId();
            Long userId = participantQueueDto.userId();
            log.info("quizId = {}", quizId);
            log.info("userId = {}", userId);

            Long rank = participantInfoQueueRepository.getRank(quizId, userId);
            log.info("rank = {}", rank);

            // 아직 자리 다 안찬 경우
            int capacity = participantInfoQueueRepository.getParticipantNumber(quizId);
            if (capacity > 0) {
                log.info("capacity = {}", capacity);
                boolean isUserTurn = rank < 10L;
                eventPublisher.publishEvent(new ParticipantQueueInfoDto(quizId, userId, rank, true, isUserTurn));
            } else {
                eventPublisher.publishEvent(new ParticipantQueueInfoDto(quizId, userId, rank, false, false));
            }
            participantInfoQueueRepository.delete(participantQueueDto);
        }

    }
}
