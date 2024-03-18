package com.quiz.global.exception.auth;

import com.quiz.exception.CustomRuntimeException;
import com.quiz.exception.enums.ErrorType;

public class AuthException extends CustomRuntimeException {

    public AuthException(ErrorType errorType) {
        super(errorType);
    }
}
