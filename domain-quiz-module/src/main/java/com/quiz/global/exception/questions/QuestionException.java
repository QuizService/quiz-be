package com.quiz.global.exception.questions;

import com.quiz.exception.CustomRuntimeException;
import com.quiz.exception.enums.ErrorType;
import lombok.Getter;

@Getter
public class QuestionException extends CustomRuntimeException {
    public QuestionException(ErrorType errorType) {
        super(errorType);
    }
}
