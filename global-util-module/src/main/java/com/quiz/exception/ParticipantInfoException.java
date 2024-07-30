package com.quiz.exception;

import com.quiz.exception.code.ErrorCode;

public class ParticipantInfoException extends CustomRuntimeException {
    public ParticipantInfoException(ErrorCode errorCode) {
        super(errorCode);
    }
}
