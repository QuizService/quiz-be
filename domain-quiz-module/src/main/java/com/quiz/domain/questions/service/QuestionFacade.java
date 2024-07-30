package com.quiz.domain.questions.service;

import com.quiz.domain.answers.entity.Answers;
import com.quiz.domain.answers.service.AnswersService;
import com.quiz.domain.choice.entity.Choices;
import com.quiz.domain.choice.service.ChoicesService;
import com.quiz.domain.questions.dto.QuestionsRequestDto;
import com.quiz.domain.questions.dto.QuestionsResponseAdminDto;
import com.quiz.domain.questions.dto.QuestionsResponseDto;
import com.quiz.domain.questions.dto.QuestionsUpdateDto;
import com.quiz.domain.questions.entity.QuestionType;
import com.quiz.domain.questions.entity.Questions;
import com.quiz.domain.quiz.entity.Quiz;
import com.quiz.domain.quiz.service.QuizService;
import com.quiz.domain.users.entity.Users;
import com.quiz.domain.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(value = "mongoTx")
@RequiredArgsConstructor
@Service
public class QuestionFacade {
    private final QuizService quizService;
    private final QuestionService questionService;
    private final ChoicesService choicesService;
    private final AnswersService answersService;
    private final UsersService usersService;

    public void saveQuestions(List<QuestionsRequestDto> questionsDtos, Long quizId, String email) {
        Long userId = getUserId(email);

        quizService.checkQuizOwnerIsUser(userId, quizId);

        for (QuestionsRequestDto questionDto : questionsDtos) {
            QuestionType questionType = QuestionType.findByInitial(questionDto.getQuestionType());
            List<Choices> choices = choicesService.getChoicesFromDtos(questionDto, questionType);
            Answers answers = answersService.getAnswersFromDto(questionDto, questionType);

            questionService.save(choices, answers, questionDto, quizId);
            saveMaxScore(questionsDtos, quizId);
        }
    }

    public void saveQuestion(QuestionsRequestDto questionDto, Long quizId, String email) {
        Long userId = getUserId(email);

        quizService.checkQuizOwnerIsUser(userId, quizId);

        QuestionType questionType = QuestionType.findByInitial(questionDto.getQuestionType());
        List<Choices> choices = choicesService.getChoicesFromDtos(questionDto, questionType);
        Answers answers = answersService.getAnswersFromDto(questionDto, questionType);

        questionService.save(choices, answers, questionDto, quizId);
    }

    public void updateQuestions(QuestionsUpdateDto questionsDtos, Long quizId, String email) {
        Long userId = getUserId(email);

        quizService.checkQuizOwnerIsUser(userId, quizId);
        for (QuestionsRequestDto questionsDto : questionsDtos.getQuestionRequestDtos()) {
            if (questionsDto.getQuestionId() != null) {
                Questions questions = questionService.findById(questionsDto.getQuestionId());
                QuestionType newQuestionType = QuestionType.findByInitial(questionsDto.getQuestionType());

                //dto to entity
                List<Choices> newChoices = choicesService.getChoicesFromDtos(questionsDto, newQuestionType);
                Answers newAnswers = answersService.getAnswersFromDto(questionsDto, newQuestionType);

                //update
                updateChoices(questions, newChoices);
                updateAnswer(questions, newAnswers);
                questionService.update(questionsDto, questions.getId());
            } else {
                //save
                saveQuestion(questionsDto, quizId, email);
            }
        }
        saveMaxScore(questionsDtos.getQuestionRequestDtos(), quizId);
        deleteQuestions(questionsDtos.getRemoveQuestionIds());
    }

    public void saveMaxScore(List<QuestionsRequestDto> questionsRequestDtos, Long quizId) {
        Integer maxScore = calculateQuestionsTotalScore(questionsRequestDtos);
        quizService.saveQuizMaxScore(quizId, maxScore);
    }


    public void updateChoices(Questions questions, List<Choices> newChoices) {
        List<Choices> choices = questions.getChoices();
        boolean isChanged = choicesService.isChoicesChanged(choices, newChoices);
        if (isChanged) {
            questionService.updateChoices(questions.getId(), newChoices);
        }
    }

    public void updateAnswer(Questions questions, Answers newAnswers) {
        Answers answers = questions.getAnswers();
        boolean isChanged = answersService.isAnswersChanged(answers, newAnswers);
        if (isChanged) {
            questionService.updateAnswers(questions.getId(), newAnswers);
        }
    }

    @Transactional(readOnly = true)
    public List<QuestionsResponseDto> findPageByEndpoint(String endpoint) {
        Quiz quiz = quizService.findByEndpoint(endpoint);
        return questionService.findResponseByQuizId(quiz.getIdx());
    }

    @Transactional(readOnly = true)
    public List<QuestionsResponseAdminDto> findAllByQuizId(Long quizId, String email) {
        Long userId = getUserId(email);

        quizService.checkQuizOwnerIsUser(userId, quizId);
        return questionService.findResponseForAdminByQuizId(quizId);
    }

    public Integer calculateQuestionsTotalScore(List<QuestionsRequestDto> questionsDtos) {
        return questionsDtos.stream()
                .mapToInt(QuestionsRequestDto::getScore)
                .sum();
    }

    public void deleteQuestions(List<String> questionIds) {
        questionService.deleteQuestionsByIds(questionIds);
    }

    private Long getUserId(String email) {
        Users user = usersService.findByEmail(email);
        return user.getId();
    }
}
