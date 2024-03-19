package com.quiz.domain.quiz.service;

import com.quiz.domain.questions.service.QuestionFacade;
import com.quiz.domain.quiz.entity.Quiz;
import com.quiz.domain.questions.dto.QuestionIntegratedDto;
import com.quiz.domain.questions.dto.QuestionsRequestDto;
import com.quiz.domain.quiz.dto.QuizRequestDto;
import com.quiz.domain.quiz.dto.QuizResponseDto;
import com.quiz.utils.TimeConverter;
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

    public QuizResponseDto findById(Long quizId) {
        Quiz quiz = quizService.findById(quizId);
        return QuizResponseDto.builder()
                .quizId(quiz.getIdx())
                .title(quiz.getTitle())
                .maxScore(quiz.getMaxScore())
                .startDate(TimeConverter.localDateTimeToString(quiz.getStartDate()))
                .dueDate(TimeConverter.localDateTimeToString(quiz.getDueDate()))
                .build();
    }
}
