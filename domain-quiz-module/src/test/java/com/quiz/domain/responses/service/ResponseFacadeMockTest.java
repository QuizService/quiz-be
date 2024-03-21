package com.quiz.domain.responses.service;

import com.quiz.TestConfiguration;
import com.quiz.domain.response.dto.ResponsesRequestDto;
import com.quiz.domain.response.service.ResponsesFacade;
import com.quiz.global.event.CustomEventListener;
import com.quiz.global.mock.TestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;

@Slf4j
@Testcontainers
@ContextConfiguration(classes = {TestConfiguration.class})
@ExtendWith(MockitoExtension.class)
public class ResponseFacadeMockTest {
    @Mock
    private ResponsesFacade responsesFacade;
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

    @Test
    void testSaveRes() {
        // Given
        CustomEventListener customEventListener = new CustomEventListener(responsesFacade);
        Map<String, Object> params = new HashMap<>();
        List<ResponsesRequestDto> responses = List.of(TestDto.getResponseRequestDto("aaaaaaa"));
        String participantInfoId = "participantInfoId";
        Long quizId = 123L;
        params.put("responses", responses);
        params.put("participantInfoId", participantInfoId);
        params.put("quizId", quizId);

        // When
        customEventListener.saveResponses(params);

        // Then
        verify(responsesFacade).calculateScoreAndSaveResponse(quizId, responses, participantInfoId);
    }
}
