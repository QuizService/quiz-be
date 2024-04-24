package com.quiz.domain.quiz.service;

import com.quiz.domain.participantsinfo.service.ParticipantInfoQueueService;
import com.quiz.domain.questions.entity.Questions;
import com.quiz.domain.questions.service.QuestionService;
import com.quiz.domain.quiz.dto.QuizRequestDto;
import com.quiz.domain.quiz.dto.QuizResponseDto;
import com.quiz.domain.quiz.entity.Quiz;
import com.quiz.utils.TimeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional(value = "mongoTx")
@RequiredArgsConstructor
@Service
public class QuizFacade {
    private static final String url = "http://localhost:8080/form/";

    private final QuizService quizService;
    private final QuestionService questionService;
    private final ParticipantInfoQueueService participantInfoQueueService;

    public Long saveQuiz(QuizRequestDto quizDto, Long userId) {
        Long quizId = quizService.saveQuiz(quizDto, userId);
        // 생성 시 대기열 생성
        participantInfoQueueService.createQuizQueue(quizId, quizDto.getCapacity());
        return quizId;
    }

    public Long updateQuiz(QuizRequestDto quizDto, Long quizId, Long userId) {
        quizService.checkQuizOwnerIsUser(userId, quizId);
        return quizService.update(quizDto, quizId);
    }

    public String findEndpointByQuizId(Long quizId) {
        String endpoint = quizService.findEndPointById(quizId);
        return url + endpoint;
    }

    public QuizResponseDto findByEndPoint(String endpoint) {
        Quiz quiz = quizService.findByEndpoint(endpoint);
        return toDto(quiz, null);
    }

    public Page<QuizResponseDto> findAllByUserId(Long userId, int page, int size) {
        return quizService.findAllByUserId(userId, page, size);
    }

    public QuizResponseDto findById(Long quizId) {
        Quiz quiz = quizService.findById(quizId);
        List<Questions> questionsList = questionService.findByQuizId(quizId);
        boolean isQuestionsCreated = !questionsList.isEmpty();
        return toDto(quiz, isQuestionsCreated);
    }

    private QuizResponseDto toDto(Quiz quiz, Boolean isQuestionsCreated) {
        if(isQuestionsCreated != null) {
            return QuizResponseDto.builder()
                    .quizId(quiz.getIdx())
                    .title(quiz.getTitle())
                    .maxScore(quiz.getMaxScore())
                    .startDate(TimeConverter.localDateTimeToString(quiz.getStartDate()))
                    .dueDate(TimeConverter.localDateTimeToString(quiz.getDueDate()))
                    .isQuestionsCreated(isQuestionsCreated)
                    .created(TimeConverter.localDateTimeToString(quiz.getCreated()))
                    .build();
        } return QuizResponseDto.builder()
                .quizId(quiz.getIdx())
                .title(quiz.getTitle())
                .maxScore(quiz.getMaxScore())
                .startDate(TimeConverter.localDateTimeToString(quiz.getStartDate()))
                .dueDate(TimeConverter.localDateTimeToString(quiz.getDueDate()))
                .created(TimeConverter.localDateTimeToString(quiz.getCreated()))
                .build();


    }

}
