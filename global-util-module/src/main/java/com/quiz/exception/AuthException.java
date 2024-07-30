package com.quiz.exception;

import com.quiz.exception.code.ErrorCode;

public class AuthException extends CustomRuntimeException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
