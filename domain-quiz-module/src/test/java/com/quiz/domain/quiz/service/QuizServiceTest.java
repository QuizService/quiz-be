package com.quiz.domain.quiz.service;

import com.quiz.TestConfiguration;
import com.quiz.domain.quiz.entity.Quiz;
import com.quiz.domain.quiz.dto.QuizRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
@Testcontainers
@Slf4j
@ContextConfiguration(classes = {TestConfiguration.class})
@SpringBootTest
public class QuizServiceTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:latest"))
            .withExposedPorts(27017);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongoDBContainer.getReplicaSetUrl("quiz"));
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
        registry.add("spring.data.mongodb.username", () -> "admin");
        registry.add("spring.data.mongodb.password", () -> "password");
        registry.add("spring.data.mongodb.database", () -> "quiz");
    }


    @Autowired
    private QuizService quizService;

    private Long userId = 1L;

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
        QuizRequestDto quizRequestDto = QuizRequestDto.builder()
                .title("test")
                .capacity(10)
                .startDate("2025-02-02 12:00:00")
                .dueDate("2025-02-05 12:00:00")
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
