package com.quiz.domain.quiz.service;


import com.quiz.domain.quiz.dto.QuizRequestDto;
import com.quiz.domain.quiz.entity.Quiz;
import com.quiz.domain.quiz.mongo.QuizMongoTemplate;
import com.quiz.domain.quiz.mongo.QuizRepository;
import com.quiz.global.SequenceGenerator;
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
    private final QuizMongoTemplate quizMongoTemplate;
    private final SequenceGenerator sequenceGenerator;


    @Transactional
    public Long saveQuiz(QuizRequestDto request, Long userId) {
        Quiz quiz = Quiz.builder()
                .idx(sequenceGenerator.generateSequence(Quiz.SEQUENCE_NAME))
                .userId(userId)
                .title(request.getTitle())
                .capacity(request.getCapacity())
                .startDate(request.getStartDate())
                .dueDate(request.getDueDate())
                .build();
        return quizRepository.save(quiz).getIdx();
    }

    public Long update(QuizRequestDto request, Long quizId) {
        Quiz quiz = quizRepository.findByIdx(quizId)
                .orElseThrow(() -> new QuizException(QUIZ_NOT_FOUND));
        quiz.update(request);
        quizMongoTemplate.update(quiz);
        return quiz.getIdx();
    }

    @Transactional(readOnly = true)
    public String findEndPointById(Long quizId) {
        Quiz quiz = quizRepository.findByIdx(quizId)
                .orElseThrow(() -> new NoSuchElementException("quiz endpoint not found"));
        return quiz.getEndpoint();
    }

    protected void saveQuizMaxScore(Long quizId, Integer maxScore) {
        Quiz quiz = quizRepository.findByIdx(quizId)
                .orElseThrow(() -> new QuizException(QUIZ_NOT_FOUND));

        quiz.setMaxScore(maxScore);
        quizMongoTemplate.updateMaxScore(quiz);
    }

    public Quiz findById(Long quizId) {
        return quizRepository.findByIdx(quizId)
                .orElseThrow(() -> new QuizException(QUIZ_NOT_FOUND));
    }

    // for test
    public void deleteAll() {
        quizRepository.deleteAll();
    }


}
