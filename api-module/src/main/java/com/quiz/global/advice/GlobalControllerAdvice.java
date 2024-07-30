package com.quiz.global.advice;

import com.quiz.dto.ResponseDto;
import com.quiz.exception.CustomRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<ResponseDto<?>> customExceptionHandler(CustomRuntimeException ex) {
        ResponseDto<?> res = ResponseDto.failed(String.valueOf(ex.getErrorCode().getCode()), ex.getErrorCode().getMessage());

        return ResponseEntity
                .status(ex.getErrorCode().getCode())
                .body(res);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<?>> exceptionHandler(Exception ex) {
        log.error("internal server err : {}", ex.getMessage());
        ResponseDto<?> res = ResponseDto.failed("500", "Internal Server Error");

        return ResponseEntity
                .status(500)
                .body(res);
    }
}
