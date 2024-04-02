package com.quiz.domain.participantsinfo.service;

import com.quiz.domain.participantsinfo.dto.ParticipantsRankResponseDto;
import com.quiz.domain.participantsinfo.entity.ParticipantInfo;
import com.quiz.domain.participantsinfo.repository.mongo.ParticipantInfoMongoTemplate;
import com.quiz.domain.participantsinfo.repository.mongo.ParticipantInfoRepository;
import com.quiz.domain.participantsinfo.repository.redis.ParticipantInfoQueueRepository;
import com.quiz.global.lock.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(value = "mongoTx")
@Service
public class ParticipantInfoService {
    private final ParticipantInfoMongoTemplate participantInfoMongoTemplate;
    private final ParticipantInfoRepository participantInfoRepository;
    private final ParticipantInfoQueueRepository participantInfoQueueRepository;

    @DistributedLock(key = "'saveFcfs : quizId - ' + #quizId")
    public String saveFcfs(Long quizId, Long userId, int capacity) {
        int participatedCnt = participantInfoMongoTemplate.countParticipantsByQuizId(quizId);
        if(participatedCnt + 1 > capacity) {
            throw new RuntimeException("cannot participate");
        }
        return save(quizId, userId);
    }

    @DistributedLock(key = "'updateFcFs : quizId - ' + #quizId")
    public void updateFcFs(Long quizId, Long userId, int capacity) {
        int participatedCnt = participantInfoMongoTemplate.countParticipantsByQuizId(quizId);
        log.info("participantCnt = {}", participatedCnt);
        if(participatedCnt + 1 > capacity) {
            throw new RuntimeException("cannot participate2");
        }
        participantInfoMongoTemplate.update(quizId, userId, participatedCnt + 1);
        // 수용 가능 인원 - 참여 인원 - 1 저장
        log.info("capacity = {}", capacity - participatedCnt - 1);
        participantInfoQueueRepository.setParticipantNumber(quizId, capacity - participatedCnt - 1);
    }

    public int countParticipantInfoCntByQuizId(Long quizId) {
        return participantInfoMongoTemplate.countParticipantsByQuizId(quizId);
    }


    public String save(Long quizId, Long userId) {
        ParticipantInfo participantInfo = ParticipantInfo.builder()
                .quizId(quizId)
                .userId(userId)
                .build();
        participantInfo = participantInfoRepository.save(participantInfo);

        return participantInfo.getId();
    }

    public ParticipantInfo findByQuizIdAndUserId(Long quizId, Long userId) {
        return participantInfoMongoTemplate.findByQuizIdAndUserId(quizId, userId)
                .orElseThrow(() -> new RuntimeException("participantInfo not found"));
    }

    public boolean isUserParticipated(Long quizId, Long userId) {
        return participantInfoMongoTemplate.findByQuizIdAndUserId(quizId, userId).isPresent();
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

    public List<ParticipantInfo> getParticipantInfos() {
        return participantInfoMongoTemplate.findAll();
    }

    // for Test
    public void deleteAll() {
        participantInfoMongoTemplate.deleteAll();
    }
}
