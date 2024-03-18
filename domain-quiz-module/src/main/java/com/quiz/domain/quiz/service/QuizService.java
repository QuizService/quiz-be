package com.quiz.domain.quiz.service;


import com.quiz.domain.quiz.entity.Quiz;
import com.quiz.domain.quiz.repository.QuizRepository;
import com.quiz.domain.quiz.dto.QuizRequestDto;
import com.quiz.global.exception.quiz.QuizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static com.quiz.global.exception.quiz.enums.QuizErrorType.QUIZ_NOT_FOUND;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class QuizService {
    private final QuizRepository quizRepository;


    @Transactional
    public Long saveQuiz(QuizRequestDto request, Long userId) {
        Quiz quiz = Quiz.builder()
                .userId(userId)
                .title(request.getTitle())
                .capacity(request.getCapacity())
                .startDate(request.getStartDate())
                .dueDate(request.getDueDate())
                .build();
        Quiz quiz1 = quizRepository.save(quiz);
        log.info("save quiz, quiz = {}", quiz1);
        return quiz1.getId();
    }

    public Long update(QuizRequestDto request, Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizException(QUIZ_NOT_FOUND));
        quiz.update(request);
        quiz = quizRepository.save(quiz);
        return quiz.getId();
    }

    @Transactional(readOnly = true)
    public String findEndPointById(Long quizId) {
        return quizRepository.findEndpointById(quizId)
                .orElseThrow(() -> new NoSuchElementException("quiz endpoint not found"));
    }

    protected void saveQuizMaxScore(Long quizId, Integer maxScore) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizException(QUIZ_NOT_FOUND));

        quiz.setMaxScore(maxScore);
        quizRepository.save(quiz);
    }

    public Quiz findById(Long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizException(QUIZ_NOT_FOUND));
    }

    // for test
    public void deleteAll() {
        quizRepository.deleteAll();
    }


}
