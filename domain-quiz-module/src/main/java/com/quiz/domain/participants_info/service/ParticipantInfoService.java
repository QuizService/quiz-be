package com.quiz.domain.participants_info.service;

import com.quiz.domain.participants_info.entity.ParticipantInfo;
import com.quiz.domain.participants_info.mongo.ParticipantInfoMongoTemplate;
import com.quiz.lock.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ParticipantInfoService {
    private final ParticipantInfoMongoTemplate participantInfoMongoTemplate;

    @DistributedLock()
    public String saveFcfs(Long quizId, Long userId, int capacity) {
        int participated = participantInfoMongoTemplate.countParticipantsByQuizId(quizId);
        if(participated + 1 > capacity) {
            throw new RuntimeException("cannot participate");
        }
        return save(quizId, userId);
    }

    public String save(Long quizId, Long userId) {
        ParticipantInfo participantInfo = ParticipantInfo.builder()
                .quizId(quizId)
                .userId(userId)
                .build();
        return participantInfoMongoTemplate.save(participantInfo);
    }
}
