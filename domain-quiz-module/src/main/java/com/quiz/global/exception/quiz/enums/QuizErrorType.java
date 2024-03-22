package com.quiz.global.exception.quiz.enums;

import com.quiz.exception.enums.ErrorType;

public enum QuizErrorType implements ErrorType {
    QUIZ_TYPE_NOT_FOUND("quiz type not found", 400)
    ,MAXSCORE_CANNOT_BE_MINUS("maxScore cannot be minus", 400)
    ,QUIZ_NOT_FOUND("quiz not found", 404)
    ,CANNOT_UPDATE_AFTER_START_DATE("cannot update after startDate", 400)
    ,QUIZ_OWNER_NOT_MATCH("quiz owner not match", 403)
    ;

    private final String message;
    private final int code;

    QuizErrorType(String message, int code) {
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
