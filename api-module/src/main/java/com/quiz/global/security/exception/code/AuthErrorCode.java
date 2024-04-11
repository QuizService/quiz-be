package com.quiz.global.security.exception.code;

import com.quiz.exception.enums.ErrorType;

public enum AuthErrorCode implements ErrorType {
    JWT_NOT_VALID("jwt not valid", 403)
    , ACCESS_TOKEN_NOT_EXIST("access token not exist", 403)
    , REFRESH_TOKEN_NOT_EXIST("refresh token not exist", 403)
    ;
    private final String message;
    private final int code;

    AuthErrorCode(String message, int code) {
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