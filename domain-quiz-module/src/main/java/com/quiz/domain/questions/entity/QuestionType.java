package com.quiz.domain.questions.entity;


import com.quiz.enums.CodeValue;
import com.quiz.exception.QuizException;
import com.quiz.exception.code.QuizErrorCode;

import java.util.Arrays;

public enum QuestionType implements CodeValue {
    MULTIPLE_CHOICE("M","선택형"),
    SHORT_ANSWER("S","단답형");

    private final String code;
    private final String value;

    QuestionType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static QuestionType findByType(String type) {
        return Arrays.stream(values())
                .filter(questionType -> questionType.value.equals(type))
                .findFirst()
                .orElseThrow(() -> new QuizException(QuizErrorCode.QUIZ_TYPE_NOT_FOUND));
    }

    public static QuestionType findByInitial(String initial) {
        return Arrays.stream(values())
                .filter(questionType -> questionType.code.equals(initial))
                .findFirst()
                .orElseThrow(() -> new QuizException(QuizErrorCode.QUIZ_TYPE_NOT_FOUND));
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
