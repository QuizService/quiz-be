package com.quiz.global.exception.participantinfo;

import com.quiz.exception.CustomRuntimeException;
import com.quiz.exception.enums.ErrorType;

public class ParticipantInfoException extends CustomRuntimeException {
    public ParticipantInfoException(ErrorType errorType) {
        super(errorType);
    }
}
