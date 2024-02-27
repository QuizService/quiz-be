package com.quiz.global.exception.answers;

import com.quiz.exception.CustomRuntimeException;
import com.quiz.exception.enums.ErrorType;
import lombok.Getter;

@Getter
public class AnswersException extends CustomRuntimeException {
    public AnswersException(ErrorType errorType) {
        super(errorType);
    }
}
