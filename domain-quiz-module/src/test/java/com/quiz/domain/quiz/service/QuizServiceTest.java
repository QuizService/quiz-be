package com.quiz.domain.quiz.service;

import com.quiz.TestConfiguration;
import com.quiz.domain.quiz.entity.Quiz;
import com.quiz.domain.quiz.dto.QuizRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {TestConfiguration.class})
@SpringBootTest
public class QuizServiceTest {

    @Autowired
    private QuizService quizService;

    @AfterEach
    void clear() {
        quizService.deleteAll();
    }

    @Test
    void saveQuizTest() {
        Long quizId =  save();
        assertThat(quizId)
                .isNotNull();
    }

    Long save() {
        Long userId = 1L;
        QuizRequestDto quizRequestDto = QuizRequestDto.builder()
                .title("test")
                .capacity(10)
                .startDate("2024-02-02 12:00:00")
                .dueDate("2024-02-05 12:00:00")
                .build();
        return quizService.saveQuiz(quizRequestDto, userId);
    }

    @Test
    void saveQuizMaxScore() {
        Long quizId = save();
        Integer maxScore = 100;

        quizService.saveQuizMaxScore(quizId, maxScore);
        Quiz quiz = quizService.findById(quizId);

        assertThat(quiz.getMaxScore())
                .isEqualTo(maxScore);

    }

    @Test
    void findEndpointTest() {
        Long quizId = save();
        String endPoint = quizService.findEndPointById(quizId);

        assertThat(endPoint)
                .isNotNull();
        assertThat(endPoint)
                .isNotBlank();
        assertThat(endPoint.length())
                .isEqualTo(8);

    }
}
