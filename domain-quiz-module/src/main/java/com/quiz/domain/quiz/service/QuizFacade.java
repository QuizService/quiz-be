package com.quiz.domain.quiz.service;

import com.quiz.domain.participantsinfo.service.ParticipantInfoQueueService;
import com.quiz.domain.questions.entity.Questions;
import com.quiz.domain.questions.service.QuestionService;
import com.quiz.domain.quiz.dto.QuizRequestDto;
import com.quiz.domain.quiz.dto.QuizResponseDto;
import com.quiz.domain.quiz.entity.Quiz;
import com.quiz.domain.users.service.UsersService;
import com.quiz.utils.TimeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Transactional(value = "mongoTx")
@RequiredArgsConstructor
@Service
public class QuizFacade {

    private final QuizService quizService;
    private final QuestionService questionService;
    private final ParticipantInfoQueueService participantInfoQueueService;
    private final UsersService usersService;

    public Long saveQuiz(QuizRequestDto quizDto, String email) {
        Long userId = getUserId(email);

        Long quizId = quizService.saveQuiz(quizDto, userId);
        // 생성 시 대기열 생성
        participantInfoQueueService.createQuizQueue(quizId, quizDto.getCapacity());
        return quizId;
    }

    public Long updateQuiz(QuizRequestDto quizDto, Long quizId, String email) {
        Long userId = getUserId(email);

        quizService.checkQuizOwnerIsUser(userId, quizId);
        return quizService.update(quizDto, quizId);
    }

    public String findEndpointByQuizId(Long quizId) {
        return quizService.findEndPointById(quizId);
    }

    public QuizResponseDto findByEndPoint(String endpoint) {
        Quiz quiz = quizService.findByEndpoint(endpoint);
        return toDto(quiz, null);
    }

    public Page<QuizResponseDto> findAllByUserId(String email, int page, int size) {
        Long userId = getUserId(email);

        Page<Quiz> quizzes = quizService.findAllByUserId(userId, page, size);
        List<Long> quizIds = quizzes.stream().map(Quiz::getIdx)
                .toList();
        Map<Long, Integer> questionCntByQuizIds = questionService.findQuestionsCntByQuizId(quizIds);

        return quizzes.map(quiz -> QuizResponseDto.builder()
                .quizId(quiz.getIdx())
                .title(quiz.getTitle())
                .capacity(quiz.getCapacity())
                .maxScore(quiz.getMaxScore())
                .startDate(TimeConverter.localDateTimeToString(quiz.getStartDate()))
                .dueDate(TimeConverter.localDateTimeToString(quiz.getDueDate()))
                .isQuestionsCreated(questionCntByQuizIds.getOrDefault(quiz.getIdx(), 0) != 0)
                .build());
    }

    public QuizResponseDto findById(Long quizId) {
        Quiz quiz = quizService.findById(quizId);
        List<Questions> questionsList = questionService.findByQuizId(quizId);
        boolean isQuestionsCreated = !questionsList.isEmpty();
        return toDto(quiz, isQuestionsCreated);
    }

    private QuizResponseDto toDto(Quiz quiz, Boolean isQuestionsCreated) {
        if (isQuestionsCreated != null) {
            return QuizResponseDto.builder()
                    .quizId(quiz.getIdx())
                    .title(quiz.getTitle())
                    .maxScore(quiz.getMaxScore())
                    .capacity(quiz.getCapacity())
                    .startDate(TimeConverter.localDateTimeToString(quiz.getStartDate()))
                    .dueDate(TimeConverter.localDateTimeToString(quiz.getDueDate()))
                    .isQuestionsCreated(isQuestionsCreated)
                    .created(TimeConverter.localDateTimeToString(quiz.getCreated()))
                    .build();
        }
        return QuizResponseDto.builder()
                .quizId(quiz.getIdx())
                .title(quiz.getTitle())
                .maxScore(quiz.getMaxScore())
                .capacity(quiz.getCapacity())
                .startDate(TimeConverter.localDateTimeToString(quiz.getStartDate()))
                .dueDate(TimeConverter.localDateTimeToString(quiz.getDueDate()))
                .created(TimeConverter.localDateTimeToString(quiz.getCreated()))
                .build();
    }

    private Long getUserId(String email) {
        return usersService.findByEmail(email).getId();
    }
}
