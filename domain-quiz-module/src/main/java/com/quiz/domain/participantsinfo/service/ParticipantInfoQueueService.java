package com.quiz.domain.participantsinfo.service;

import com.quiz.domain.participantsinfo.repository.redis.ParticipantInfoQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ParticipantInfoQueueService {
    private final ParticipantInfoQueueRepository participantInfoQueueRepository;

    public Long addQueue(Long quizId, Long userId) {
        return participantInfoQueueRepository.addQueue(quizId, userId);

    }

    public void createQuizQueue(Long quizId, int capacity) {
        participantInfoQueueRepository.setParticipantNumber(quizId, capacity);
    }

    public Long getRank(Long quizId, Long userId) {
        return participantInfoQueueRepository.getRank(quizId, userId);
    }


}
