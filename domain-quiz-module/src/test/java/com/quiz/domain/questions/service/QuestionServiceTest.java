package com.quiz.domain.questions.service;

import com.quiz.domain.answers.entity.Answers;
import com.quiz.domain.choice.entity.Choices;
import com.quiz.domain.questions.dto.QuestionsRequestDto;
import com.quiz.domain.questions.entity.QuestionType;
import com.quiz.domain.questions.entity.Questions;
import com.quiz.domain.questions.repository.mongo.QuestionsMongoTemplate;
import com.quiz.domain.questions.repository.mongo.QuestionsRepository;
import com.quiz.global.mock.TestDto;
import com.quiz.global.mock.TestEntities;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {
    @Mock
    private QuestionsRepository questionsRepository;

    @Mock
    private QuestionsMongoTemplate questionsMongoTemplate;

    @InjectMocks
    private QuestionService questionService;

    private static final String FAKE_QUESTION_ID = "testQuestionId";

    @AfterEach
    void clear() {
        questionService.deleteAll();
    }

    @Test
    void saveTest() {
        QuestionsRequestDto questionsRequestDto = TestDto.getQuestionsReqDto();
        Questions questions = TestEntities.getQuestions();
        questions.setIdForTest(FAKE_QUESTION_ID);

        Mockito.when(questionsRepository.save(Mockito.any(Questions.class)))
                .thenReturn(questions);
        String questionId = questionService.save(TestEntities.getChoices(), TestEntities.getAnswers(), questionsRequestDto, 1L);

        assertThat(questionId)
                .isNotNull();
    }

    @Test
    void updateTest() {
        String questionId = FAKE_QUESTION_ID;
        QuestionsRequestDto newQuestionRequestDto = TestDto.getUpdatedQuestionsReqDto();

        LocalDateTime now = LocalDateTime.now();

        questionsMongoTemplate.updateQuestion(questionId, newQuestionRequestDto, now);
        Mockito.verify(questionsMongoTemplate)
                .updateQuestion(questionId, newQuestionRequestDto, now);

        questionService.update(newQuestionRequestDto, questionId);

        Mockito.when(questionsRepository.findById(questionId))
                .thenReturn(Optional.of(Questions.builder().title("updated question1")
                        .score(20)
                        .sequence(2)
                        .questionType("S")
                        .answers(TestEntities.getAnswers())
                        .build()));
        Questions questions = questionService.findById(questionId);

        assertThat(questions.getQuestionType()).isEqualTo(QuestionType.SHORT_ANSWER);
        assertThat(questions.getTitle()).isEqualTo(newQuestionRequestDto.getTitle());
        assertThat(questions.getScore()).isEqualTo(newQuestionRequestDto.getScore());
    }

    @Test
    void updateChoiceTest() {
        String questionId = FAKE_QUESTION_ID;
        List<Choices> updatedChoices = TestEntities.getMultipleUpdateChoices();

        questionsMongoTemplate.updateChoices(questionId, updatedChoices);
        Mockito.verify(questionsMongoTemplate)
                .updateChoices(questionId, updatedChoices);

        questionService.updateChoices(questionId, updatedChoices);
    }

    @Test
    void updateAnswerTest() {
        String questionId = FAKE_QUESTION_ID;

        Answers updatedAnswer = TestEntities.getNewMultipleAnswers();

        questionsMongoTemplate.updateAnswers(questionId, updatedAnswer);
        Mockito.verify(questionsMongoTemplate)
                .updateAnswers(questionId, updatedAnswer);

        questionService.updateAnswers(questionId, updatedAnswer);
    }
}
