package com.quiz.global.queue;

import com.quiz.domain.participantsinfo.dto.ParticipantQueueDto;
import com.quiz.domain.participantsinfo.repository.redis.ParticipantInfoQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@EnableScheduling // 추가
@Component
@RequiredArgsConstructor
public class QueueScheduler {
    private final ParticipantInfoQueueRepository participantInfoQueueRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final SimpMessagingTemplate messagingTemplate;

    @Async
    @Transactional(value = "redisTx1")
    @Scheduled(fixedDelay = 2000)
    public void queue() {
        Set<ParticipantQueueDto> queue = participantInfoQueueRepository.getTenUsers();

        for (ParticipantQueueDto participantQueueDto : queue) {
            Long quizId = participantQueueDto.quizId();
            Long userId = participantQueueDto.userId();
            log.info("quizId = {}, userId = {}", quizId, userId);

            Long rank = participantInfoQueueRepository.getRank(quizId, userId);
            rank = rank == null ? 0 : rank;

            Integer capacity = participantInfoQueueRepository.getParticipantNumber(quizId);
            if (capacity > 0) { // 아직 자리 다 안찬 경우
                log.info("capacity = {}", capacity);
                boolean isUserTurn = rank < 10L;
                eventPublisher.publishEvent(new ParticipantQueueInfoDto(quizId, userId, rank, true, isUserTurn));
            } else {
                eventPublisher.publishEvent(new ParticipantQueueInfoDto(quizId, userId, rank, false, false));
            }
            participantInfoQueueRepository.delete(participantQueueDto);
        }
    }

    @Async
    @Transactional(value = "redisTx1")
    @Scheduled(fixedDelay = 2000)
    public void showRank() {
        Set<ParticipantQueueDto> queue = participantInfoQueueRepository.getAllUsers();

        for (ParticipantQueueDto participantQueueDto : queue) {
            Long quizId = participantQueueDto.quizId();
            Long userId = participantQueueDto.userId();

            Long rank = participantInfoQueueRepository.getRank(quizId, userId);
            rank = rank == null ? 0 : rank;
            log.info("userId = {}, rank = {}", userId, rank);
            //        String endpoint = String.format("?quiz-id=%d&user-id=%d",quizId, userId);
            String endpoint = String.format("?quiz-id=%d", quizId);

            messagingTemplate.convertAndSend("/topic/rank" + endpoint, rank);
        }
    }
}
