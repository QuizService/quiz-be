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
    private final Redis1Utils redis1Utils;

    private static final String WAITING_QUEUE_KEY_PREFIX = "WAITING_QUIZ_ID";
    private static final String PARTICIPANT_NUMBER_KEY_PREFIX = "PARTICIPANT_IN_QUIZ_ID";
    private static final Long START_IDX = 0L;
    private static final Long END_IDX = 10L;


    // 대기열 큐에 추가
    public Long addQueue(Long quizId, Long userId) {
        Long time = System.currentTimeMillis();

        ParticipantQueueDto queue = toValue(userId, quizId);

        redis1Utils.addQueue(WAITING_QUEUE_KEY_PREFIX, queue, time);
        return redis1Utils.getZRank(WAITING_QUEUE_KEY_PREFIX, queue);
    }

    // 현재 대기 상태
    public Long getRank(Long quizId, Long userId) {
        ParticipantQueueDto queue = toValue(userId, quizId);
        return redis1Utils.getZRank(WAITING_QUEUE_KEY_PREFIX, queue);
    }

    public List<ParticipantQueueDto> getUserFromQueue() {
        Set<Object> usersSet = redis1Utils.zRange(WAITING_QUEUE_KEY_PREFIX, START_IDX, END_IDX);
        return usersSet.stream()
                .map(i -> (ParticipantQueueDto) i)
                .toList();
    }

    public Set<ParticipantQueueDto> getTenUsers() {
        Long size = redis1Utils.getZSetSize(WAITING_QUEUE_KEY_PREFIX);
        Set<Object> usersSet = redis1Utils.zRange(WAITING_QUEUE_KEY_PREFIX, START_IDX, size < 10L ? size : 10L);
        return usersSet.stream()
                .map(i -> (ParticipantQueueDto) i)
                .collect(Collectors.toSet());
    }

    public Set<ParticipantQueueDto> getAllUsers() {
        Long size = redis1Utils.getZSetSize(WAITING_QUEUE_KEY_PREFIX);
        Set<Object> usersSet = redis1Utils.zRange(WAITING_QUEUE_KEY_PREFIX, START_IDX, size);
        return usersSet.stream()
                .map(i -> (ParticipantQueueDto) i)
                .collect(Collectors.toSet());
    }

    public void delete(ParticipantQueueDto queue) {
//        redis1Utils.deleteRange(WAITING_QUEUE_KEY_PREFIX, START_IDX, END_IDX);
        redis1Utils.delete(WAITING_QUEUE_KEY_PREFIX,queue);
    }

    public void setParticipantNumber(Long quizId, int leftCapacity) {
        String key = PARTICIPANT_NUMBER_KEY_PREFIX + quizId;
        redis1Utils.setValue(key, (long) leftCapacity);
    }

    public Integer getParticipantNumber(Long quizId) {
        String key = PARTICIPANT_NUMBER_KEY_PREFIX + quizId;
        return (Integer) redis1Utils.getValue(key);
    }

    private static ParticipantQueueDto toValue(Long userId, Long quizId) {
        return ParticipantQueueDto.builder()
                .quizId(quizId)
                .userId(userId)
                .build();
    }

    public Long getZSetSize() {
        return redis1Utils.getZSetSize(WAITING_QUEUE_KEY_PREFIX);
    }



}
