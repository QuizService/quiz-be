package com.quiz.domain.questions.service;

import com.quiz.TestConfiguration;
import com.quiz.domain.questions.mock.TestDto;
import com.quiz.domain.questions.mock.TestEntities;
import com.quiz.dto.questions.QuestionsRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ContextConfiguration(classes = {TestConfiguration.class})
@SpringBootTest
public class QuestionServiceTest {

    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:latest"))
            .withExposedPorts(27017);

    static {
        mongoDBContainer.start();
    }

    @Autowired
    QuestionService questionService;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.url", () -> mongoDBContainer.getReplicaSetUrl("quiz"));
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
        registry.add("spring.data.mongodb.username", () -> "admin");
        registry.add("spring.data.mongodb.password", () -> "password");
        registry.add("spring.data.mongodb.database", () -> "quiz");
    }

    @AfterEach
    void clear() {
        questionService.deleteAll();
    }

    @Test
    void saveTest() {
        Long questionIdx = save();
        assertThat(questionIdx)
                .isNotNull();
    }


    Long save() {
        QuestionsRequestDto questionsRequestDto = TestDto.getQuestionsReqDto();
        return questionService.save(TestEntities.getChoices(), TestEntities.getAnswers(), questionsRequestDto, 1L);
    }
}
