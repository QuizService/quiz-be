package com.quiz.exception;

import com.quiz.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public class QuestionException extends CustomRuntimeException {
    public QuestionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
