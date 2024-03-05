package com.quiz.domain.participants_info.service;

import com.quiz.domain.participants_info.repository.ParticipantInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ParticipantInfoService {

    private final ParticipantInfoRepository participantInfoRepository;

    // 추후에 quiz에 설정된 인원 제한 수 가져올 방법 생각해보기
    private int mockQuizCapacity = 100;

    public boolean canParticipantJoinQuiz(Long quizId, Long userId) {
        int count = participantInfoRepository.countParticipantsByQuizId(quizId);
        return count < mockQuizCapacity;
    }
}
