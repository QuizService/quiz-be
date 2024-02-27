package com.quiz.global.exception.questions.enums;

import com.quiz.exception.enums.ErrorType;

public enum QuestionErrorType implements ErrorType {
    QUESTION_TYPE_NOT_FOUND("question type not found")
    ,SCORE_CANNOT_BE_MINUS("score cannot be minus")
    ,QUESTION_NOT_FOUND("question not found")
    ;

    private final String message;

    QuestionErrorType(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
