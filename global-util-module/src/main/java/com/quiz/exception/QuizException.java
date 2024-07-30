package com.quiz.exception;

import com.quiz.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public class QuizException extends CustomRuntimeException {
    public QuizException(ErrorCode errorCode) {
        super(errorCode);
    }
}
