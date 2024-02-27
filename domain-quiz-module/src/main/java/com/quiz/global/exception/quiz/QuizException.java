package com.quiz.global.exception.quiz;

import com.quiz.exception.CustomRuntimeException;
import com.quiz.exception.enums.ErrorType;
import lombok.Getter;

@Getter
public class QuizException extends CustomRuntimeException {
    public QuizException(ErrorType errorType) {
        super(errorType);
    }
}
