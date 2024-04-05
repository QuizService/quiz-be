package com.quiz.global.security.exception;

import com.quiz.exception.CustomRuntimeException;
import com.quiz.exception.enums.ErrorType;

public class AuthException extends CustomRuntimeException {
    public AuthException(ErrorType errorType) {
        super(errorType);
    }
}
