package com.quiz.domain.quiz.service;


import com.quiz.domain.quiz.dto.QuizRequestDto;
import com.quiz.domain.quiz.entity.Quiz;
import com.quiz.domain.quiz.repository.mongo.QuizMongoTemplate;
import com.quiz.domain.quiz.repository.mongo.QuizRepository;
import com.quiz.global.sequence.SequenceGenerator;
import com.quiz.global.exception.quiz.QuizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static com.quiz.global.exception.quiz.code.QuizErrorCode.QUIZ_NOT_FOUND;
import static com.quiz.global.exception.quiz.code.QuizErrorCode.QUIZ_OWNER_NOT_MATCH;

@Slf4j
@Transactional(value = "mongoTx")
@RequiredArgsConstructor
@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizMongoTemplate quizMongoTemplate;
    private final SequenceGenerator sequenceGenerator;


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

    public void saveQuizMaxScore(Long quizId, Integer maxScore) {
        Quiz quiz = quizRepository.findByIdx(quizId)
                .orElseThrow(() -> new QuizException(QUIZ_NOT_FOUND));

        quiz.setMaxScore(maxScore);
        quizMongoTemplate.updateMaxScore(quiz);
    }

    public Quiz findById(Long quizId) {
        return quizRepository.findByIdx(quizId)
                .orElseThrow(() -> new QuizException(QUIZ_NOT_FOUND));
    }

    public Quiz findByEndpoint(String endpoint) {
        return quizRepository.findByEndpoint(endpoint)
                .orElseThrow(() -> new QuizException(QUIZ_NOT_FOUND));
    }

    public boolean isUserIsQuizOwner(Long userId, Long quizId) {
        Quiz quiz = findById(quizId);
        return quiz.getUserId().equals(userId);
    }

    public void checkQuizOwnerIsUser(Long userId, Long quizId) {
        Quiz quiz = findById(quizId);
        if(!quiz.getUserId().equals(userId)) {
            throw new QuizException(QUIZ_OWNER_NOT_MATCH);
        }
    }

    public Page<Quiz> findAllByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "created");
        return quizMongoTemplate.findQuizByUserId(userId, pageable);

    }

    public List<Quiz> findAllExpiredQuiz() {
        return quizMongoTemplate.findAllAfterDueDateQuiz();
    }

    // for test
    public void deleteAll() {
        quizRepository.deleteAll();
    }

}
