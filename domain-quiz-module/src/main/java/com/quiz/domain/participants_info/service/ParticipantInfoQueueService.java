package com.quiz.domain.participants_info.service;

import com.quiz.domain.participants_info.repository.redis.ParticipantInfoQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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

    public Long getRank(Long quizId, Long userId) {
        return participantInfoQueueRepository.getRank(quizId, userId);
    }

    /*
    * 스케쥴러에서 redis sorted set 순회하면서 클라이언트에게 메시지 전달
    *
    * */
}
