package com.quiz.domain.responses.service;

import com.quiz.TestConfiguration;
import com.quiz.domain.participants_info.entity.ParticipantInfo;
import com.quiz.domain.participants_info.service.ParticipantInfoService;
import com.quiz.domain.questions.service.QuestionService;
import com.quiz.domain.quiz.service.QuizService;
import com.quiz.domain.response.service.ResponsesFacade;
import com.quiz.global.mock.TestDto;
import com.quiz.global.mock.TestEntities;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RecordApplicationEvents
@Testcontainers
@ContextConfiguration(classes = {TestConfiguration.class})
@SpringBootTest
public class ResponseFacadeTest {
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
    ApplicationEvents applicationEvent;

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

    @BeforeEach
    void setUp() {
        quizId = quizService.saveQuiz(TestDto.getQuizRequestDto(), userId);
        questionId = questionService.save(TestEntities.getChoices(), TestEntities.getAnswers(), TestDto.getQuestionsReqDto(), quizId);
        participantInfoId = participantInfoService.save(quizId, participantId);
    }

    @AfterEach
    void clear() {
        participantInfoService.deleteAll();
        questionService.deleteAll();
        quizService.deleteAll();
    }

    @Test
    void saveResponseTest() {
        responsesFacade.saveResponse(quizId, participantId, List.of(TestDto.getResponseRequestDto(questionId)));
    }

}
