package com.quiz.global.exception.auth;

import com.quiz.exception.enums.ErrorType;
import lombok.Getter;

@Getter
public enum AuthErrorCode implements ErrorType {
    INVALID_AUTH_TYPE("유효하지 않은 Auth type")
    ;

    private final String message;

    AuthErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
