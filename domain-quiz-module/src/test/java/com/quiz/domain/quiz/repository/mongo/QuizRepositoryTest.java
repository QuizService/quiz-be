package com.quiz.domain.quiz.repository.mongo;

import com.quiz.domain.quiz.dto.QuizRequestDto;
import com.quiz.domain.quiz.entity.Quiz;
import com.quiz.global.exception.quiz.QuizException;
import com.quiz.global.exception.quiz.code.QuizErrorCode;
import com.quiz.global.mock.TestDto;
import com.quiz.global.mock.TestEntities;
import com.quiz.utils.TimeConverter;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Testcontainers
@ExtendWith(SpringExtension.class)
@DataMongoTest
public class QuizRepositoryTest {
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    static {
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongoDBContainer.getReplicaSetUrl("quiz"));
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
        registry.add("spring.data.mongodb.username", () -> "admin");
        registry.add("spring.data.mongodb.password", () -> "password");
        registry.add("spring.data.mongodb.database", () -> "quiz");
        registry.add("spring.data.mongodb.authentication-database", () -> "admin");
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private QuizRepository quizRepository;

    private QuizMongoTemplate quizMongoTemplate;

    @BeforeEach
    void setUp() {
        quizMongoTemplate = new QuizMongoTemplate(mongoTemplate);
    }

    @Test
    void updateTest() {
        Quiz quiz = quizRepository.save(TestEntities.getQuiz());

        QuizRequestDto requestDto = TestDto.getQuizRequestDto();

        quiz.update(requestDto);
        quizMongoTemplate.update(quiz);

        Quiz updateQuiz = quizRepository.findById(quiz.getId())
                .orElseThrow(() -> new QuizException(QuizErrorCode.QUIZ_NOT_FOUND));

        assertThat(updateQuiz.getTitle())
                .isEqualTo(requestDto.getTitle());
        assertThat(updateQuiz.getCapacity())
                .isEqualTo(requestDto.getCapacity());
        assertThat(updateQuiz.getStartDate())
                .isEqualTo(TimeConverter.stringToLocalDateTime(requestDto.getStartDate()));
    }
}
