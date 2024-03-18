package com.quiz.domain.participants_info.service;

import com.quiz.domain.participants_info.dto.ParticipantsRankResponseDto;
import com.quiz.domain.participants_info.entity.ParticipantInfo;
import com.quiz.domain.participants_info.mongo.ParticipantInfoMongoTemplate;
import com.quiz.lock.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ParticipantInfoService {
    private final ParticipantInfoMongoTemplate participantInfoMongoTemplate;

    @DistributedLock()
    public String saveFcfs(Long quizId, Long userId, int capacity) {
        int participatedCnt = participantInfoMongoTemplate.countParticipantsByQuizId(quizId);
        if(participatedCnt + 1 > capacity) {
            throw new RuntimeException("cannot participate");
        }
        return save(quizId, userId);
    }

    public void updateFcFs(Long quizId, Long userId, int capacity) {
        int participatedCnt = participantInfoMongoTemplate.countParticipantsByQuizId(quizId);
        if(participatedCnt + 1 > capacity) {
            throw new RuntimeException("cannot participate2");
        }
        participantInfoMongoTemplate.update(quizId, userId, capacity + 1);
    }


    public String save(Long quizId, Long userId) {
        ParticipantInfo participantInfo = ParticipantInfo.builder()
                .quizId(quizId)
                .userId(userId)
                .build();
        return participantInfoMongoTemplate.save(participantInfo);
    }

    public ParticipantInfo findByQuizIdAndUserId(Long quizId, Long userId) {
        return participantInfoMongoTemplate.findByQuizIdAndUserId(quizId, userId)
                .orElseThrow(() -> new RuntimeException("participantInfo not found"));
    }

    public void updateTotalScore(String participantId, Integer score) {
        participantInfoMongoTemplate.updateScore(participantId, score);
    }

    public List<ParticipantInfo> findParticipantInfoByQuizId(Long quizId) {
        return participantInfoMongoTemplate.findAllByQuizIdOrderByNumber(quizId);
    }

    public List<ParticipantsRankResponseDto> findRanksByQuizId(Long quizId) {
        return participantInfoMongoTemplate.findParticipantsRankResponsesByQuizIdOrderByNumber(quizId);
    }
}
