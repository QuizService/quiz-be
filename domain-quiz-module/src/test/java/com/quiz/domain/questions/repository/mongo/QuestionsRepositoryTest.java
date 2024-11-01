package com.quiz.domain.questions.repository.mongo;


import com.quiz.domain.answers.entity.Answers;
import com.quiz.domain.choice.entity.Choices;
import com.quiz.domain.questions.dto.QuestionsRequestDto;
import com.quiz.domain.questions.entity.QuestionType;
import com.quiz.domain.questions.entity.Questions;
import com.quiz.global.exception.questions.QuestionException;
import com.quiz.global.exception.questions.code.QuestionErrorCode;
import com.quiz.global.mock.TestDto;
import com.quiz.global.mock.TestEntities;
import org.junit.jupiter.api.AfterEach;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ExtendWith(SpringExtension.class)
@DataMongoTest
public class QuestionsRepositoryTest {

    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    static {
        mongoDBContainer.start();
    }

    private final Long quizId = 1L;
    @Autowired
    private QuestionsRepository questionsRepository;

    private QuestionsMongoTemplate questionsMongoTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

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

    @BeforeEach
    void setUp() {
        questionsMongoTemplate = new QuestionsMongoTemplate(mongoTemplate);
    }

    @AfterEach
    void clear() {
        questionsRepository.deleteAll();
    }

    @Test
    void saveTest() {
        save();
        System.out.println("test success");
    }

    @Test
    void updateQuestionsTest() {
        Questions questions = TestEntities.getQuestions();
        String questionId = questionsRepository.save(questions).getId();

        QuestionsRequestDto requestDto = TestDto.getUpdatedQuestionsReqDto();
        questionsMongoTemplate.updateQuestion(questionId, requestDto, LocalDateTime.now());

        Questions updatedQuestions = questionsRepository.findById(questionId)
                .orElseThrow(() -> new QuestionException(QuestionErrorCode.QUESTION_NOT_FOUND));

        assertThat(updatedQuestions.getTitle())
                .isEqualTo(requestDto.getTitle());
        assertThat(updatedQuestions.getScore())
                .isEqualTo(requestDto.getScore());
        assertThat(updatedQuestions.getQuestionType().getCode())
                .isEqualTo(requestDto.getQuestionType());
    }

    @Test
    void updateChoicesTest() {
        Questions questions = TestEntities.getQuestions();
        String questionId = questionsRepository.save(questions).getId();

        List<Choices> updateChoices = TestEntities.getMultipleUpdateChoices();

        questionsMongoTemplate.updateChoices(questionId, updateChoices);

        Questions updatedQuestions = questionsRepository.findById(questionId)
                .orElseThrow(() -> new QuestionException(QuestionErrorCode.QUESTION_NOT_FOUND));

        assertThat(updatedQuestions.getChoices().get(0).getTitle())
                .contains("updatechoice ");
    }

    @Test
    void updateAnswersTest() {
        Questions questions = TestEntities.getQuestions();
        String questionId = questionsRepository.save(questions).getId();

        Answers updateAnswer = TestEntities.getNewMultipleAnswers();
        questionsMongoTemplate.updateAnswers(questionId, updateAnswer);

        Questions updatedQuestions = questionsRepository.findById(questionId)
                .orElseThrow(() -> new QuestionException(QuestionErrorCode.QUESTION_NOT_FOUND));
        assertThat(updatedQuestions.getAnswers().getMultipleChoiceAnswers())
                .isEqualTo(updateAnswer.getMultipleChoiceAnswers());

    }

    @Test
    void findByQuizId() {
        String id = save();
        List<Questions> questions = questionsRepository.findAllByQuizId(quizId);

        assertThat(questions)
                .isNotEmpty();
        Questions question = questions.get(0);
        assertThat(question.getQuestionType())
                .isEqualTo(QuestionType.MULTIPLE_CHOICE);
        assertThat(question.getChoices().size())
                .isEqualTo(TestEntities.getChoices().size());
        assertThat(question.getAnswers().getMultipleChoiceAnswers())
                .containsExactly(TestEntities.ANSWER_NUMBER);


    }

    @Test
    void findByIdxTest() {
        String id = save();
        Questions question = questionsRepository.findById(id)
                .orElseThrow(() -> new QuestionException(QuestionErrorCode.QUESTION_NOT_FOUND));
        assertThat(question.getQuestionType())
                .isEqualTo(QuestionType.MULTIPLE_CHOICE);
        assertThat(question.getChoices().size())
                .isEqualTo(TestEntities.getChoices().size());
        assertThat(question.getAnswers().getMultipleChoiceAnswers())
                .containsExactly(TestEntities.ANSWER_NUMBER);
    }

    String save() {
        Questions questions = TestEntities.getQuestions();
        return questionsRepository.save(questions).getId();
    }

}
