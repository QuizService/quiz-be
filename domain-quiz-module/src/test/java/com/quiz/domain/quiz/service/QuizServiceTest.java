package com.quiz.domain.quiz.service;

import com.quiz.MongoDbTestConfig;
import com.quiz.TestConfiguration;
import com.quiz.dto.quiz.QuizRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ContextConfiguration(classes = {TestConfiguration.class, MongoDbTestConfig.class})
@ActiveProfiles("test")
@SpringBootTest
public class QuizServiceTest {

    @Autowired
    private QuizService quizService;
    private Long quizId;

    @Test
    void test() {
        System.out.println("test success");
    }

    @Test
    void saveQuizTest() {
        quizId = save();
        Assertions.assertThat(quizId)
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
}
