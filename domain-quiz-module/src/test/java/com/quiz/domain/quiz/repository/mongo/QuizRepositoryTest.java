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
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

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
    private static final Long USER_ID = 1L;
    private static final String QUIZ_TITLE_PREFIX = "quiz ";
    private static final Integer QUIZ_CAPACITY = 10;
    private static final String QUIZ_START_DATE = "2024-10-10 00:00:00";
    private static final String QUIZ_DUE_DATE = "2024-10-11 00:00:00";

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
    void saveQuizTest() {
        Quiz quiz = TestEntities.getQuiz();
        Quiz savedQuiz = quizRepository.save(quiz);

        assertThat(savedQuiz.getId())
                .isNotNull();
        assertThat(savedQuiz.getTitle())
                .isEqualTo(quiz.getTitle());
        assertThat(savedQuiz.getCapacity())
                .isEqualTo(quiz.getCapacity());
        assertThat(savedQuiz.getStartDate())
                .isEqualTo(quiz.getStartDate());
        assertThat(savedQuiz.getDueDate())
                .isEqualTo(quiz.getDueDate());
    }

    @Test
    void updateTest() {
        Quiz savedQuiz = savedQuiz();

        QuizRequestDto requestDto = TestDto.getQuizRequestDto();

        savedQuiz.update(requestDto);
        quizMongoTemplate.update(savedQuiz);

        Quiz updateQuiz = quizRepository.findById(savedQuiz.getId())
                .orElseThrow(() -> new QuizException(QuizErrorCode.QUIZ_NOT_FOUND));

        assertThat(updateQuiz.getTitle())
                .isEqualTo(requestDto.getTitle());
        assertThat(updateQuiz.getCapacity())
                .isEqualTo(requestDto.getCapacity());
        assertThat(updateQuiz.getStartDate())
                .isEqualTo(TimeConverter.stringToLocalDateTime(requestDto.getStartDate()));
    }

    @Test
    void updateMaxScore() {
        Integer newMaxScore = 200;
        Quiz quiz = savedQuiz();
        quiz.setMaxScore(newMaxScore);

        quizMongoTemplate.updateMaxScore(quiz);
        assertThat(quiz.getMaxScore())
                .isEqualTo(newMaxScore);
    }

    @Test
    @DisplayName("유저가 만든 퀴즈들 페이지 단위로 조회")
    void findQuizByUserIdTest() {
        saveMultiQuizzes();
        int pageSize = 10;
        Pageable pageable = PageRequest.of(0, pageSize, Sort.Direction.DESC, "created");

        Page<Quiz> quizPage = quizMongoTemplate.findQuizByUserId(USER_ID, pageable);
        List<Quiz> quizList = quizPage.getContent();

        assertThat(quizList.size())
                .isEqualTo(pageSize);
        for(int i = 0; i<10; i++) {
            assertThat(quizList.get(i).getTitle())
                    .contains(QUIZ_TITLE_PREFIX);
            assertThat(quizList.get(i).getCapacity())
                    .isEqualTo(QUIZ_CAPACITY);
            assertThat(quizList.get(i).getStartDate())
                    .isEqualTo(TimeConverter.stringToLocalDateTime(QUIZ_START_DATE));
            assertThat(quizList.get(i).getDueDate())
                    .isEqualTo(TimeConverter.stringToLocalDateTime(QUIZ_DUE_DATE));
        }
    }

    void saveMultiQuizzes() {
        List<Quiz> quizList = new ArrayList<>();
        for(int i = 0; i<15; i++) {
            Quiz quiz = Quiz.builder()
                    .idx((long)(i + 1))
                    .userId(USER_ID)
                    .title(QUIZ_TITLE_PREFIX + i)
                    .capacity(QUIZ_CAPACITY)
                    .startDate(QUIZ_START_DATE)
                    .dueDate(QUIZ_DUE_DATE)
                    .build();
            quizList.add(quiz);
        }
        quizRepository.saveAll(quizList);
    }

    @NotNull
    private Quiz savedQuiz() {
        Quiz quiz = TestEntities.getQuiz();
        return quizRepository.save(quiz);
    }
}
