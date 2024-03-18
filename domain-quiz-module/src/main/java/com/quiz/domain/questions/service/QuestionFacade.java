package com.quiz.domain.questions.service;

import com.quiz.domain.answers.entity.Answers;
import com.quiz.domain.answers.service.AnswersService;
import com.quiz.domain.choice.entity.Choices;
import com.quiz.domain.choice.service.ChoicesService;
import com.quiz.domain.questions.entity.QuestionType;
import com.quiz.domain.questions.entity.Questions;
import com.quiz.domain.questions.dto.QuestionsRequestDto;
import com.quiz.domain.questions.dto.QuestionsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class QuestionFacade {
    private final QuestionService questionService;
    private final ChoicesService choicesService;
    private final AnswersService answersService;

    public void saveQuestions(List<QuestionsRequestDto> questionsDtos, Long quizId) {
        for (QuestionsRequestDto questionDto : questionsDtos) {
            QuestionType questionType = QuestionType.findByInitial(questionDto.getQuestionType());
            List<Choices> choices = choicesService.getChoicesFromDtos(questionDto, questionType);
            Answers answers = answersService.getAnswersFromDto(questionDto, questionType);

            questionService.save(choices, answers, questionDto, quizId);
        }
    }

    public void updateQuestions(List<QuestionsRequestDto> questionsDtos, Long quizId) {
        for (QuestionsRequestDto questionsDto : questionsDtos) {
            if(questionsDto.getQuestionId() != null) {
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
                saveQuestions(questionsDtos, quizId);
            }
        }
    }

    public void updateChoices(Questions questions, List<Choices> newChoices) {
        List<Choices> choices= questions.getChoices();
        boolean isChanged = choicesService.isChoicesChanged(choices, newChoices);
        if(isChanged) {
            questionService.updateChoices(questions.getId(), newChoices);
        }
    }

    public void updateAnswer(Questions questions, Answers newAnswers) {
        Answers answers = questions.getAnswers();
        boolean isChanged = answersService.isAnswersChanged(answers, newAnswers);
        if(isChanged) {
            questionService.updateAnswers(questions.getId(), newAnswers);
        }


    }

    @Transactional(readOnly = true)
    public Page<QuestionsResponseDto> findPageByQuizId(Long quizId, int page, int size) {
        return questionService.findResponseByQuizId(quizId, page, size);
    }

    public Integer calculateQuestionsTotalScore(List<QuestionsRequestDto> questionsDtos) {
        return questionsDtos.stream()
                .mapToInt(QuestionsRequestDto::getScore)
                .sum();
    }
}
