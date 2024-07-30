package com.quiz.exception.code;

public enum AnswersErrorCode implements ErrorCode {
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
