package com.quiz.domain.participantsinfo.repository.redis;

import com.quiz.domain.participantsinfo.dto.ParticipantQueueDto;
import com.quiz.global.db.redis.Redis1Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantInfoQueueRepository {
    private static final String WAITING_QUEUE_KEY_PREFIX = "WAITING_QUIZ_ID_";
    private static final String PARTICIPANT_NUMBER_KEY_PREFIX = "PARTICIPANT_IN_QUIZ_ID";
    private static final String QUIZ_ID_KEY = "QUIZ_ID";
    private static final Long START_IDX = 0L;
    private static final Long END_IDX = 10L;

    private final Redis1Utils redis1Utils;

    public void addQuizInfo(Long quizId) {
        Long size = redis1Utils.getZSetSize(QUIZ_ID_KEY);
        Set<Object> quizIdRawQueue = redis1Utils.zRange(QUIZ_ID_KEY, START_IDX, size);
        Set<Long> quizIdQueue = quizIdRawQueue
                .stream()
                .map(q -> (long) q)
                .collect(Collectors.toSet());
        if (quizIdQueue.contains(quizId)) {
            Long time = System.currentTimeMillis();
            redis1Utils.addQueue(QUIZ_ID_KEY, quizId, time);
        }
    }

    public Set<Long> getQuizIdSet() {
        Long quizIdSetSize = redis1Utils.getZSetSize(QUIZ_ID_KEY);
        Set<Object> quizIdSet = redis1Utils.zRange(QUIZ_ID_KEY, START_IDX, quizIdSetSize);
        return quizIdSet.stream()
                .map(q -> (long) q)
                .collect(Collectors.toSet());
    }

    private static ParticipantQueueDto toValue(Long userId, Long quizId) {
        return ParticipantQueueDto.builder()
                .quizId(quizId)
                .userId(userId)
                .build();
    }

    // 대기열 큐에 추가
    public Long addQueue(Long quizId, Long userId) {
        Long time = System.currentTimeMillis();

        redis1Utils.addQueue(WAITING_QUEUE_KEY_PREFIX + quizId, userId, time);
        return redis1Utils.getZRank(WAITING_QUEUE_KEY_PREFIX + quizId, userId);
    }

    // 현재 대기 상태
    public Long getRank(Long quizId, Long userId) {
        return redis1Utils.getZRank(WAITING_QUEUE_KEY_PREFIX + quizId, userId);
    }

    public Set<Long> get10Users(Long quizId) {
        Long size = redis1Utils.getZSetSize(WAITING_QUEUE_KEY_PREFIX + quizId);
        Set<Object> usersSet = redis1Utils.zRange(WAITING_QUEUE_KEY_PREFIX + quizId, START_IDX, size < 10L ? size : 10L);
        return usersSet.stream()
                .map(i -> (long) i)
                .collect(Collectors.toSet());
    }

    public Set<Long> getAllUsers(Long quizId) {
        Long size = redis1Utils.getZSetSize(WAITING_QUEUE_KEY_PREFIX + quizId);
        Set<Object> usersSet = redis1Utils.zRange(WAITING_QUEUE_KEY_PREFIX + quizId, START_IDX, size);
        return usersSet.stream()
                .map(i -> (long) i)
                .collect(Collectors.toSet());
    }

    public void delete(ParticipantQueueDto queue) {
        redis1Utils.delete(WAITING_QUEUE_KEY_PREFIX + queue.quizId(), queue.userId());
    }

    public void deleteQuizIdInQueue(Long quizId) {
        redis1Utils.delete(QUIZ_ID_KEY, quizId);
    }

    public void delete10Users(Long quizId, Long cnt) {
        redis1Utils.deleteRange(WAITING_QUEUE_KEY_PREFIX + quizId, START_IDX, cnt);
    }

    public void setParticipantNumber(Long quizId, int leftCapacity) {
        String key = PARTICIPANT_NUMBER_KEY_PREFIX + quizId;
        redis1Utils.setValue(key, (long) leftCapacity);
    }

    public Integer getParticipantNumber(Long quizId) {
        String key = PARTICIPANT_NUMBER_KEY_PREFIX + quizId;
        return (Integer) redis1Utils.getValue(key);
    }

    public Long getZSetSize() {
        return redis1Utils.getZSetSize(WAITING_QUEUE_KEY_PREFIX);
    }


}
