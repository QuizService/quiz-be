package com.quiz.domain.quiz.entity;


import com.quiz.domain.quiz.dto.QuizRequestDto;
import com.quiz.global.exception.quiz.QuizException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.quiz.global.exception.quiz.enums.QuizErrorType.CANNOT_CREATE_AFTER_START_DATE;
import static com.quiz.global.exception.quiz.enums.QuizErrorType.START_DATE_CANNOT_BE_AFTER_DUE_DATE;

public class QuizTest {

    @DisplayName("Quiz 생성")
    @Test
    void createQuizConstructor() {
        Assertions.assertThatCode(() -> Quiz.builder()
                .idx(1L)
                .title("title")
                .capacity(10)
                .userId(1L)
                .startDate("2024-10-01 00:00:00")
                .dueDate("2024-10-02 00:00:00")
                .build())
                .doesNotThrowAnyException();
    }

    @DisplayName("오늘 이전으로 startDate 설정시 throws exception")
    @Test
    void createQuizStartDateIsBeforeToday() {
        Assertions.assertThatThrownBy(() -> Quiz.builder()
                        .idx(1L)
                        .title("title")
                        .capacity(10)
                        .userId(1L)
                        .startDate("2023-10-01 00:00:00")
                        .dueDate("2024-10-02 00:00:00")
                        .build())
                .isInstanceOf(QuizException.class)
                .hasMessage(CANNOT_CREATE_AFTER_START_DATE.getMessage());
    }

    @DisplayName("DueDate 이후로 startDate 설정시 throws exception")
    @Test
    void createQuizStartDateAfterDueDate() {
        Assertions.assertThatThrownBy(() -> Quiz.builder()
                        .idx(1L)
                        .title("title")
                        .capacity(10)
                        .userId(1L)
                        .startDate("2024-10-10 00:00:00")
                        .dueDate("2024-10-02 00:00:00")
                        .build())
                .isInstanceOf(QuizException.class)
                .hasMessage(START_DATE_CANNOT_BE_AFTER_DUE_DATE.getMessage());
    }

    @DisplayName("quiz update")
    @Test
    void updateQuiz() {
        Quiz quiz = Quiz.builder()
                .idx(1L)
                .title("title")
                .capacity(10)
                .userId(1L)
                .startDate("2024-10-01 00:00:00")
                .dueDate("2024-10-02 00:00:00")
                .build();

        QuizRequestDto quizRequestDto = QuizRequestDto.builder()
                .title("update title")
                .capacity(101)
                .build();

        Assertions.assertThatCode(() -> quiz.update(quizRequestDto))
                .doesNotThrowAnyException();
    }
}
