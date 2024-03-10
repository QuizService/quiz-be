package com.quiz.domain.participants_info.service;

import com.quiz.domain.participants_info.mongo.ParticipantInfoMongoTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ParticipantInfoService {

    private final ParticipantInfoMongoTemplate participantInfoMongoTemplate;

//    public boolean canParticipantJoinQuiz(Long quizId, Long userId, Integer capacity) {
//
//    }
}
