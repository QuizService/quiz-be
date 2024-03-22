package com.quiz.global.exception.answers.enums;

import com.quiz.exception.enums.ErrorType;

public enum AnswersErrorType implements ErrorType {
    ANSWERS_NOT_FOUND("answers not found",404)
    ;

    private final String message;
    private final Integer code;

    AnswersErrorType(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
