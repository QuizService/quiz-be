package com.quiz.domain.participantsinfo.service;

import com.quiz.domain.participantsinfo.entity.ParticipantInfo;
import com.quiz.domain.participantsinfo.repository.mongo.ParticipantInfoMongoTemplate;
import com.quiz.domain.participantsinfo.repository.mongo.ParticipantInfoRepository;
import com.quiz.domain.participantsinfo.repository.redis.ParticipantInfoQueueRepository;
import com.quiz.exception.ParticipantInfoException;
import com.quiz.global.lock.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.quiz.exception.code.ParticipantInfoErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParticipantInfoService {
    private final ParticipantInfoMongoTemplate participantInfoMongoTemplate;
    private final ParticipantInfoRepository participantInfoRepository;
    private final ParticipantInfoQueueRepository participantInfoQueueRepository;
    AtomicInteger cnt = new AtomicInteger();

    @DistributedLock(key = "'saveFcfs : quizId - ' + #quizId")
    public String saveFcfs(Long quizId, Long userId, int capacity) {
        Optional<ParticipantInfo> participantInfoOptional = participantInfoMongoTemplate.findByQuizIdAndUserId(quizId, userId);
        if(participantInfoOptional.isPresent()) {
            throw new ParticipantInfoException(ALREADY_PARTICIPATED);
        }

        int participatedCnt = participantInfoMongoTemplate.countParticipantsByQuizIdAndSubmitResponses(quizId);
        if (participatedCnt >= capacity) {
            log.info("failed count = {}", cnt.incrementAndGet());
            throw new ParticipantInfoException(FIRST_COME_FIRST_SERVED_END);
        }
        return save(quizId, userId);
    }

    // for test
    @DistributedLock(key = "'saveFcfs : quizId - ' + #quizId")
    public String saveFcfsTest(Long quizId, Long userId, int capacity) {
        int participatedCnt = participantInfoMongoTemplate.countParticipantsByQuizId(quizId);
        if (participatedCnt >= capacity) {
            log.info("failed count = {}", cnt.incrementAndGet());
            throw new ParticipantInfoException(FIRST_COME_FIRST_SERVED_END);
        }
        return save(quizId, userId);
    }

    @DistributedLock(key = "'updateFcFs : quizId - ' + #quizId")
    public String updateFcFs(Long quizId, Long userId, int capacity) {
        //check user participated
        ParticipantInfo participantInfo = findByQuizIdAndUserId(quizId, userId);

        int participatedCnt = participantInfoMongoTemplate.countParticipantsByQuizIdAndSubmitResponses(quizId);
        if (participatedCnt + 1 > capacity) {
            throw new ParticipantInfoException(FIRST_COME_FIRST_SERVED_END);
        }
        participantInfoMongoTemplate.update(quizId, userId, participatedCnt + 1);
        participantInfoQueueRepository.setParticipantNumber(quizId, capacity - participatedCnt - 1);

        return participantInfo.getId();
    }

    public int countParticipantInfoByQuizId(Long quizId) {
        return participantInfoMongoTemplate.countParticipantsByQuizId(quizId);
    }


    public String save(Long quizId, Long userId) {
        ParticipantInfo participantInfo = ParticipantInfo.builder()
                .quizId(quizId)
                .userId(userId)
                .submitResponses(false)
                .build();
        participantInfo = participantInfoRepository.save(participantInfo);

        return participantInfo.getId();
    }

    @Transactional(value = "mongoTx")
    public ParticipantInfo findByQuizIdAndUserId(Long quizId, Long userId) {
        return participantInfoMongoTemplate.findByQuizIdAndUserId(quizId, userId)
                .orElseThrow(() -> new ParticipantInfoException(PARTICIPANT_NOT_FOUND));
    }

    @Transactional(value = "mongoTx")
    public boolean isUserParticipated(Long quizId, Long userId) {
        return participantInfoMongoTemplate.findByQuizIdAndUserId(quizId, userId).isPresent();
    }

    @Transactional(value = "mongoTx")
    public void updateTotalScore(String participantId, Integer score) {
        participantInfoMongoTemplate.updateScore(participantId, score);
    }

    @Transactional(value = "mongoTx")
    public List<ParticipantInfo> findParticipantInfoByQuizId(Long quizId) {
        return participantInfoMongoTemplate.findAllByQuizIdOrderByNumber(quizId);
    }

    public List<ParticipantInfo> getParticipantInfos() {
        return participantInfoMongoTemplate.findAll();
    }

    // for Test
    public void deleteAll() {
        participantInfoMongoTemplate.deleteAll();
    }
}
