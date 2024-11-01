package com.quiz.domain.responses.service;

import com.quiz.TestConfiguration;
import com.quiz.domain.participantsinfo.service.ParticipantInfoService;
import com.quiz.domain.questions.service.QuestionService;
import com.quiz.domain.quiz.service.QuizService;
import com.quiz.domain.response.dto.ResponsesRequestDto;
import com.quiz.domain.response.service.ResponsesFacade;
import com.quiz.global.mock.TestDto;
import com.quiz.global.mock.TestEntities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

// Integration Test

@RecordApplicationEvents
@Testcontainers
@ContextConfiguration(classes = {TestConfiguration.class})
@SpringBootTest
public class ResponseFacadeTest {
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:latest"))
            .withExposedPorts(27017);
    static GenericContainer redisContainer = new GenericContainer(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379);
    static MySQLContainer mysqlContainer = new MySQLContainer(DockerImageName.parse("mysql:8.0"))
            .withDatabaseName("quiz")
            .withUsername("user")
            .withPassword("password");

    static {
        mongoDBContainer.start();
        redisContainer.start();
        mysqlContainer.start();
    }

    @Autowired
    QuizService quizService;
    @Autowired
    QuestionService questionService;
    @Autowired
    ParticipantInfoService participantInfoService;
    @Autowired
    ResponsesFacade responsesFacade;
    Long userId = 1L;
    Long participantId = 2L;
    private Long quizId;
    private String questionId;
    private String participantInfoId;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongoDBContainer.getReplicaSetUrl("quiz"));
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
        registry.add("spring.data.mongodb.username", () -> "admin");
        registry.add("spring.data.mongodb.password", () -> "password");
        registry.add("spring.data.mongodb.database", () -> "quiz");

        registry.add("spring.data.redis.host1", redisContainer::getHost);
        registry.add("spring.data.redis.port1", redisContainer::getFirstMappedPort);
        registry.add("spring.data.redis.host2", redisContainer::getHost);
        registry.add("spring.data.redis.port2", redisContainer::getFirstMappedPort);

        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", mysqlContainer::getDriverClassName);
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQLDialect");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @BeforeEach
    void setUp() {
        quizId = quizService.saveQuiz(TestDto.getQuizRequestDto(), userId);
        questionId = questionService.save(TestEntities.getChoices(), TestEntities.getAnswers(), TestDto.getQuestionsReqDto(), quizId);
        participantInfoService.save(quizId, participantId);
        participantInfoId = participantInfoService.findByQuizIdAndUserId(quizId, participantId)
                .getId();
    }

    @AfterEach
    void clear() {
        participantInfoService.deleteAll();
        questionService.deleteAll();
        quizService.deleteAll();
    }

    @Test
    void saveResponseTest() {
        List<ResponsesRequestDto> requestDtos = List.of(TestDto.getResponseRequestDto(questionId));
        responsesFacade.calculateScoreAndSaveResponse(quizId, requestDtos, participantInfoId);
    }

}
