package com.quiz.global.exception.answers.code;

import com.quiz.exception.enums.ErrorType;

public enum AnswersErrorCode implements ErrorType {
    ANSWERS_NOT_FOUND("answers not found",404)
    ;

    private final String message;
    private final Integer code;

    AnswersErrorCode(String message, Integer code) {
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
