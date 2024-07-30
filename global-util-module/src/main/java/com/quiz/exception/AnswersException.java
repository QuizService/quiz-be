package com.quiz.exception;

import com.quiz.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public class AnswersException extends CustomRuntimeException {
    public AnswersException(ErrorCode errorCode) {
        super(errorCode);
    }
}
