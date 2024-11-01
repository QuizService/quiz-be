package com.quiz.exception;

import com.quiz.exception.enums.ErrorType;
import lombok.Getter;

@Getter
public class CustomRuntimeException extends RuntimeException {
    private final ErrorType errorType;

    public CustomRuntimeException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
