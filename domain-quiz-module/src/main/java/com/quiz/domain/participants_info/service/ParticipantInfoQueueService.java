package com.quiz.domain.participants_info.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ParticipantInfoQueueService {
    private final ParticipantInfoQueueService participantInfoQueueService;

    public Long addQueue(Long quizId, Long userId) {
        return participantInfoQueueService.addQueue(quizId, userId);
    }

    public Long getRank(Long quizId, Long userId) {

    }

}
