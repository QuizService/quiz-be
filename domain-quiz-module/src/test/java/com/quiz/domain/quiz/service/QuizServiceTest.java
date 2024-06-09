package com.quiz.domain.quiz.service;

import com.quiz.domain.quiz.dto.QuizRequestDto;
import com.quiz.domain.quiz.entity.Quiz;
import com.quiz.domain.quiz.repository.mongo.QuizMongoTemplate;
import com.quiz.domain.quiz.repository.mongo.QuizRepository;
import com.quiz.global.mock.TestEntities;
import com.quiz.global.sequence.SequenceGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {
    @Mock
    private QuizRepository quizRepository;
    @Mock
    private QuizMongoTemplate quizMongoTemplate;
    @Mock
    private SequenceGenerator sequenceGenerator;
    @InjectMocks
    private QuizService quizService;

    private final Long userId = 1L;
    private final Long quizId = 1L;

    @AfterEach
    void clear() {
        quizService.deleteAll();
    }

    @Test
    void saveQuizTest() {
        QuizRequestDto request = QuizRequestDto.builder()
                .title("test")
                .capacity(10)
                .startDate("2025-02-02 12:00:00")
                .dueDate("2025-02-05 12:00:00")
                .build();
        Mockito.when(sequenceGenerator.generateSequence(Mockito.any(String.class)))
                .thenReturn(quizId);

        Quiz quiz = Quiz.builder()
                .idx(quizId)
                .userId(userId)
                .title(request.getTitle())
                .capacity(request.getCapacity())
                .startDate(request.getStartDate())
                .dueDate(request.getDueDate())
                .build();
        quiz.setIdForTest("quizId");
        Mockito.when(quizRepository.save(Mockito.any(Quiz.class)))
                .thenReturn(quiz);
        Long savedQuizIdx = quizService.saveQuiz(request, userId);

        assertThat(savedQuizIdx)
                .isNotNull();
    }

    @Test
    void saveQuizMaxScore() {
        Quiz quiz = TestEntities.getQuiz();
        int maxScore = 200;

        Mockito.when(quizRepository.findByIdx(Mockito.any(Long.class)))
                .thenReturn(Optional.of(quiz));
        quiz.setMaxScore(maxScore);

        quizMongoTemplate.updateMaxScore(quiz);
        Mockito.verify(quizMongoTemplate)
                .updateMaxScore(quiz);

        quizService.saveQuizMaxScore(quizId, maxScore);
    }

    @Test
    void checkQuizOwnerIsUserTest() {
        Quiz quiz = TestEntities.getQuiz();

        Mockito.when(quizRepository.findByIdx(Mockito.any(Long.class)))
                .thenReturn(Optional.of(quiz));

        quizService.checkQuizOwnerIsUser(userId, quizId);
    }
}
