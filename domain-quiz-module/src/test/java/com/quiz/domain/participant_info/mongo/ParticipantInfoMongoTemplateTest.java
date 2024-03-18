package com.quiz.domain.participant_info.mongo;

import com.quiz.TestConfiguration;
import com.quiz.domain.participants_info.mongo.ParticipantInfoMongoTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@Testcontainers
@SpringBootTest
@ContextConfiguration(classes = {TestConfiguration.class})
public class ParticipantInfoMongoTemplateTest {
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    static {
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.url", () -> mongoDBContainer.getReplicaSetUrl("quiz"));
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
        registry.add("spring.data.mongodb.username", () -> "admin");
        registry.add("spring.data.mongodb.password", () -> "password");
        registry.add("spring.data.mongodb.database", () -> "quiz");
        registry.add("spring.data.mongodb.authentication-database", () -> "admin");
    }

    private ParticipantInfoMongoTemplate participantInfoMongoTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        participantInfoMongoTemplate = new ParticipantInfoMongoTemplate(mongoTemplate);
    }

    @Test
    void findParticipantsRankResponsesByQuizIdOrderByNumberTest() {

    }
}
