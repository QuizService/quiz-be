package com.quiz.global.exception.user.code;

import com.quiz.exception.enums.ErrorType;

public enum UserErrorCode implements ErrorType {
    INVALID_AUTH_TYPE("유효하지 않은 Auth type", 400), USER_NOT_FOUND("user not found", 404);

    private final String message;
    private final int code;

    UserErrorCode(String message, int code) {
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
