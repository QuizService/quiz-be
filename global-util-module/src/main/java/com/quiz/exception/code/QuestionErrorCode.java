package com.quiz.exception.code;

public enum QuestionErrorCode implements ErrorCode {
    QUESTION_TYPE_NOT_FOUND("question type not found", 400), SCORE_CANNOT_BE_MINUS("score cannot be minus", 400), QUESTION_NOT_FOUND("question not found", 404);

    private final String message;
    private final int code;

    QuestionErrorCode(String message, int code) {
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
