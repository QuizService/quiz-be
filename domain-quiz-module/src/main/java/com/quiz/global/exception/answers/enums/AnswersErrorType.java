package com.quiz.global.exception.answers.enums;

import com.quiz.exception.enums.ErrorType;

public enum AnswersErrorType implements ErrorType {
    ANSWERS_NOT_FOUND("answers not found")
    ;

    private final String message;

    AnswersErrorType(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
