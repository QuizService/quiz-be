package com.quiz.domain.questions.service;

import com.quiz.TestConfiguration;
import com.quiz.domain.answers.entity.Answers;
import com.quiz.domain.questions.entity.QuestionType;
import com.quiz.domain.questions.entity.Questions;
import com.quiz.global.mock.TestDto;
import com.quiz.global.mock.TestEntities;
import com.quiz.domain.questions.dto.QuestionsRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
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
        String questionId = save();
        assertThat(questionId)
                .isNotNull();
    }

    @Test
    void updateTest() {
        String questionId = save();
        log.info("questionId ={}", questionId);
        QuestionsRequestDto newQuestionRequestDto = TestDto.getUpdatedQuestionsReqDto();
        questionService.update(newQuestionRequestDto, questionId);

        Questions questions = questionService.findById(questionId);

        assertThat(questions.getQuestionType()).isEqualTo(QuestionType.SHORT_ANSWER);
        assertThat(questions.getTitle()).isEqualTo(newQuestionRequestDto.getTitle());
        assertThat(questions.getScore()).isEqualTo(newQuestionRequestDto.getScore());
    }

    @Test
    void updateChoiceTest() {
        String questionId = save();

        QuestionsRequestDto newQuestionRequestDto = TestDto.getUpdatedQuestionsReqDto();
        questionService.update(newQuestionRequestDto, questionId);
        questionService.updateChoices(questionId, new ArrayList<>());

        Questions questions = questionService.findById(questionId);

        assertThat(questions.getQuestionType()).isEqualTo(QuestionType.SHORT_ANSWER);
        assertThat(questions.getChoices()).isEmpty();
    }

    @Test
    void updateAnswerTest() {
        String questionId = save();

        QuestionsRequestDto newQuestionRequestDto = TestDto.getUpdatedQuestionsReqDto();
        Answers answers = TestEntities.getNewShortAnswers();
        questionService.update(newQuestionRequestDto, questionId);
        questionService.updateAnswers(questionId, answers);
        Questions questions = questionService.findById(questionId);

        assertThat(questions.getQuestionType()).isEqualTo(QuestionType.SHORT_ANSWER);
        assertThat(questions.getAnswers().getShortAnswer()).isEqualTo(answers.getShortAnswer());
    }


    String save() {
        QuestionsRequestDto questionsRequestDto = TestDto.getQuestionsReqDto();
        return questionService.save(TestEntities.getChoices(), TestEntities.getAnswers(), questionsRequestDto, 1L);
    }


}
