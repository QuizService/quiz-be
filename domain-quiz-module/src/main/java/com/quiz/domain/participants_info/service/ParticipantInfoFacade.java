package com.quiz.domain.participants_info.service;

import com.quiz.domain.participants_info.entity.ParticipantInfo;
import com.quiz.domain.participants_info.mongo.ParticipantInfoMongoTemplate;
import com.quiz.domain.quiz.entity.Quiz;
import com.quiz.domain.quiz.service.QuizService;
import com.quiz.lock.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ParticipantInfoFacade {

    private final ParticipantInfoService participantInfoService;
    private final QuizService quizService;

    public void saveParticipants(Long quizId, Long userId) {
        Quiz quiz = quizService.findById(quizId);
        int capacity = quiz.getCapacity();
        if(capacity > 0) {
            participantInfoService.saveFcfs(quizId, userId, capacity);
            return;
        }
        participantInfoService.save(quizId, userId);
    }

}
