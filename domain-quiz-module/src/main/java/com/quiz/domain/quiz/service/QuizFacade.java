package com.quiz.domain.quiz.service;

import com.quiz.domain.quiz.entity.Quiz;
import com.quiz.domain.quiz.dto.QuizRequestDto;
import com.quiz.domain.quiz.dto.QuizResponseDto;
import com.quiz.utils.TimeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class QuizFacade {
    private final QuizService quizService;

    public Long saveQuiz(QuizRequestDto quizDto, Long userId) {
        return quizService.saveQuiz(quizDto, userId);
    }

    public Long updateQuiz(QuizRequestDto quizDto, Long quizId, Long userId) {
        quizService.checkQuizOwnerIsUser(userId, quizId);
        return quizService.update(quizDto, quizId);
    }

    public String findEndpointByQuizId(Long quizId) {
        String endpoint = quizService.findEndPointById(quizId);
        return "http://localhost:8080/form/" + endpoint;
    }

    public QuizResponseDto findByEndPoint(String endpoint) {
        Quiz quiz = quizService.findByEndpoint(endpoint);
        return toDto(quiz);
    }

    public Page<QuizResponseDto> findAllByUserId(Long userId, int page, int size) {
        return quizService.findAllByUserId(userId, page, size);
    }

    public QuizResponseDto findById(Long quizId) {
        Quiz quiz = quizService.findById(quizId);
        return toDto(quiz);
    }

    private QuizResponseDto toDto(Quiz quiz) {
        return QuizResponseDto.builder()
                .quizId(quiz.getIdx())
                .title(quiz.getTitle())
                .maxScore(quiz.getMaxScore())
                .startDate(TimeConverter.localDateTimeToString(quiz.getStartDate()))
                .dueDate(TimeConverter.localDateTimeToString(quiz.getDueDate()))
                .created(TimeConverter.localDateTimeToString(quiz.getCreated()))
                .build();
    }

}
