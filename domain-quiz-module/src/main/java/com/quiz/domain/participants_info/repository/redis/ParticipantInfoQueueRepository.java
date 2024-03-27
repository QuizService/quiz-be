package com.quiz.domain.participants_info.repository.redis;

import com.quiz.domain.participants_info.dto.ParticipantQueueDto;
import com.quiz.redis.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ParticipantInfoQueueRepository {
    private final RedisUtils redisUtils;

    private static final String WAITING_QUEUE_KEY_PREFIX = "WAITING_QUIZ_ID";
    private static final String PARTICIPANT_NUMBER_KEY_PREFIX = "PARTICIPANT_IN_QUIZ_ID";
    private static final Long START_IDX = 0L;
    private static final Long END_IDX = 10L;

    // 대기열 큐에 추가
    public Long addQueue(Long quizId, Long userId) {
        Long time = System.currentTimeMillis();

        ParticipantQueueDto queue = toValue(userId, quizId);

        redisUtils.addQueue(WAITING_QUEUE_KEY_PREFIX, queue, time);

        return redisUtils.getZRank(WAITING_QUEUE_KEY_PREFIX, userId);
    }

    // 현재 대기 상태
    public Long getRank(Long quizId, Long userId) {
        ParticipantQueueDto queue = toValue(userId, quizId);
        return redisUtils.getZRank(WAITING_QUEUE_KEY_PREFIX, queue);
    }

    public List<ParticipantQueueDto> getUserFromQueue() {
        Set<Object> usersSet = redisUtils.zRange(WAITING_QUEUE_KEY_PREFIX, START_IDX, END_IDX);
        return usersSet.stream()
                .map(i -> (ParticipantQueueDto) i)
                .toList();
    }

    public void delete() {
        redisUtils.deleteRange(WAITING_QUEUE_KEY_PREFIX, START_IDX, END_IDX);
    }

    public void setParticipantNumber(Long quizId, int leftCapacity) {
        String key = PARTICIPANT_NUMBER_KEY_PREFIX + quizId;
        redisUtils.setValue(key, (long) leftCapacity);
    }

    public Long getParticipantNumber(Long quizId) {
        String key = PARTICIPANT_NUMBER_KEY_PREFIX + quizId;
        return (Long) redisUtils.getValue(key);
    }

    private static ParticipantQueueDto toValue(Long userId, Long quizId) {
        return ParticipantQueueDto.builder()
                .quizId(quizId)
                .userId(userId)
                .build();
    }



}
