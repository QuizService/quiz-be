package com.quiz.global.queue;

import com.quiz.domain.participantsinfo.dto.ParticipantQueueDto;
import com.quiz.domain.participantsinfo.repository.redis.ParticipantInfoQueueRepository;
import com.quiz.domain.quiz.entity.Quiz;
import com.quiz.domain.quiz.repository.mongo.QuizMongoTemplate;
import com.quiz.domain.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@EnableScheduling
@Component
@RequiredArgsConstructor
public class QueueScheduler {
    private final QuizService quizService;
    private final ParticipantInfoQueueRepository participantInfoQueueRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final SimpMessagingTemplate messagingTemplate;

    @Async
    @Transactional(value = "redisTx1")
    @Scheduled(fixedDelay = 2000)
    public void showRankAndPollUser() {
        Set<Long> quizIdSet = participantInfoQueueRepository.getQuizIdSet();

        // Todo : 병렬 실행으로 ㄱㄱ
        for (Long quizId : quizIdSet) {
            Set<Long> allUsersInQuiz = participantInfoQueueRepository.getAllUsers(quizId);
            Set<Long> tenUsersInQuiz = participantInfoQueueRepository.get10Users(quizId);
            for (Long userId : allUsersInQuiz) {
                Long rank = participantInfoQueueRepository.getRank(quizId, userId);
                rank = rank == null ? 0 : rank;
                log.info("userId = {}, rank = {}", userId, rank);
                String endpoint = String.format("?quiz-id=%d", quizId);

                messagingTemplate.convertAndSend("/topic/rank" + endpoint, rank);

                if (tenUsersInQuiz.contains(userId)) {
                    Integer capacity = participantInfoQueueRepository.getParticipantNumber(quizId);
                    if (capacity > 0) {
                        log.info("capacity = {}", capacity);
                        boolean isUserTurn = rank < 10L;
                        eventPublisher.publishEvent(new ParticipantQueueInfoDto(quizId, userId, rank, true, isUserTurn));
                    } else {
                        eventPublisher.publishEvent(new ParticipantQueueInfoDto(quizId, userId, rank, false, false));
                    }
                }
                participantInfoQueueRepository.delete10Users(quizId, (long) tenUsersInQuiz.size());
            }

        }
    }

    @Async
    @Scheduled(cron = "55 23 * * *")
    public void deleteExpiredQuizFromQueue() {
        List<Quiz> expiredQuizList = quizService.findAllExpiredQuiz();
        List<Long> expiredQuizIds = expiredQuizList.stream()
                .map(Quiz::getIdx)
                .toList();
        for (Long expiredQuizId : expiredQuizIds) {
            participantInfoQueueRepository.deleteQuizIdInQueue(expiredQuizId);
        }
    }
}
