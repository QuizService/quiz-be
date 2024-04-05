package com.quiz.global.exception.user;

import com.quiz.exception.CustomRuntimeException;
import com.quiz.exception.enums.ErrorType;

public class UserException extends CustomRuntimeException {

    public UserException(ErrorType errorType) {
        super(errorType);
    }
}
