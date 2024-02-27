package com.quiz.global.exception.quiz.enums;

import com.quiz.exception.enums.ErrorType;

public enum QuizErrorType implements ErrorType {
    QUIZ_TYPE_NOT_FOUND("quiz type not found")
    ,MAXSCORE_CANNOT_BE_MINUS("maxScore cannot be minus")
    ,QUIZ_NOT_FOUND("quiz not found")
    ,CANNOT_UPDATE_AFTER_START_DATE("cannot update after startDate")
    ;

    private final String message;

    QuizErrorType(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
