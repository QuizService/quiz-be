package com.quiz.domain.quiz.service;

import com.quiz.domain.questions.service.QuestionFacade;
import com.quiz.dto.questions.QuestionIntegratedDto;
import com.quiz.dto.questions.QuestionsRequestDto;
import com.quiz.dto.quiz.QuizRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class QuizFacade {
    private final QuestionFacade questionFacade;
    private final QuizService quizService;

    public Long saveQuiz(QuizRequestDto quizDto, Long userId) {
        return quizService.saveQuiz(quizDto, userId);
    }

    public Long updateQuiz(QuizRequestDto quizDto, Long quizId) {
        return quizService.update(quizDto, quizId);
    }

    public String saveQuestionsAndReturnQuizEndpoint(QuestionIntegratedDto questionIntegratedDto, Long quizId) {

        questionFacade.saveQuestions(questionIntegratedDto.getQuestionRequestDtos(), quizId);

        //save quizMaxScore
        saveQuizMaxScore(questionIntegratedDto.getQuestionRequestDtos(), quizId);
        return quizService.findEndPointById(quizId);
    }

    private void saveQuizMaxScore(List<QuestionsRequestDto> questionsRequestDtos, Long quizId) {
        Integer maxScore = questionFacade.calculateQuestionsTotalScore(questionsRequestDtos);
        quizService.saveQuizMaxScore(quizId, maxScore);
    }
}
