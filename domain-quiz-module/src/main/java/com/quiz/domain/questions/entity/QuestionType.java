package com.quiz.domain.questions.entity;


import com.quiz.global.exception.quiz.QuizException;
import com.quiz.global.exception.quiz.enums.QuizErrorType;
import lombok.Getter;

import java.util.Arrays;
@Getter
public enum QuestionType {
    MULTIPLE_CHOICE("M","선택형"),
    SHORT_ANSWER("S","단답형");

    private final String initial;
    private final String type;

    QuestionType(String initial, String type) {
        this.initial = initial;
        this.type = type;
    }

    public static QuestionType findByType(String type) {
        return Arrays.stream(values())
                .filter(questionType -> questionType.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new QuizException(QuizErrorType.QUIZ_TYPE_NOT_FOUND));
    }

    public static QuestionType findByInitial(String initial) {
        return Arrays.stream(values())
                .filter(questionType -> questionType.initial.equals(initial))
                .findFirst()
                .orElseThrow(() -> new QuizException(QuizErrorType.QUIZ_TYPE_NOT_FOUND));
    }
}
