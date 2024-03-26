package com.quiz.domain.questions.repository.mongo;


import com.quiz.TestConfiguration;
import com.quiz.domain.answers.entity.Answers;
import com.quiz.domain.choice.entity.Choices;
import com.quiz.domain.questions.repository.mongo.QuestionsMongoTemplate;
import com.quiz.domain.questions.repository.mongo.QuestionsRepository;
import com.quiz.global.mock.TestEntities;
import com.quiz.domain.questions.entity.QuestionType;
import com.quiz.domain.questions.entity.Questions;
import com.quiz.global.exception.questions.QuestionException;
import com.quiz.global.exception.questions.enums.QuestionErrorType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Testcontainers
@SpringBootTest
@ContextConfiguration(classes = {TestConfiguration.class})
public class QuestionsRepositoryTest {

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
    private QuestionsRepository questionsRepository;

    private QuestionsMongoTemplate questionsMongoTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        questionsMongoTemplate = new QuestionsMongoTemplate(mongoTemplate);
    }

    @AfterEach
    void clear() {
        questionsRepository.deleteAll();
    }

    private final Long quizId = 1L;

    @Test
    void saveTest() {
        save();
        System.out.println("test success");
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
                .orElseThrow(() -> new QuestionException(QuestionErrorType.QUESTION_NOT_FOUND));
        assertThat(question.getQuestionType())
                .isEqualTo(QuestionType.MULTIPLE_CHOICE);
        assertThat(question.getChoices().size())
                .isEqualTo(TestEntities.getChoices().size());
        assertThat(question.getAnswers().getMultipleChoiceAnswers())
                .containsExactly(TestEntities.ANSWER_NUMBER);
    }

    @Test
    void updateChoicesAndAnswers() {
        String id = save();
        List<Choices> newChoices = TestEntities.getMultipleUpdateChoices();
        Answers newAnswer = TestEntities.getNewMultipleAnswers();
        questionsRepository.updateChoices(id, newChoices);
        questionsRepository.updateAnswers(id, newAnswer);
        Questions questions = questionsRepository.findById(id)
                .orElseThrow(() -> new QuestionException(QuestionErrorType.QUESTION_NOT_FOUND));
        List<Choices> updatedChoices = questions.getChoices();
        assertThat(updatedChoices.size())
                .isEqualTo(5);
        assertThat(updatedChoices)
                .filteredOn(choice -> choice.getTitle().startsWith("updatechoice"))
                        .hasSize(5);
        List<Integer> answerChoices = updatedChoices.stream()
                        .filter(Choices::getIsAnswer)
                                .mapToInt(Choices::getSequence)
                                        .boxed().toList();
        assertThat(answerChoices)
                .containsExactly(1,4);
        assertThat(questions.getAnswers().getMultipleChoiceAnswers())
                .containsExactly(1,4);

    }

    String save() {
        Questions questions = TestEntities.getQuestions();
        return questionsRepository.save(questions).getId();
    }

}
