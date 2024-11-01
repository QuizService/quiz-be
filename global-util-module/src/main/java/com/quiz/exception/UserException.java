package com.quiz.exception;

import com.quiz.exception.code.ErrorCode;

public class UserException extends CustomRuntimeException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
