package com.quiz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<D> {
    private final Integer status;
    private final String code;
    private final String message;
    private final String timestamp;
    private final D data;

    @Builder
    public ResponseDto(Integer status, String code, String message, String timestamp, D data) {
        this.status = status == null ? 200 : status;
        this.code = code;
        this.message = message;
        this.timestamp = timestamp == null ? LocalDateTime.now().toString() : timestamp;
        this.data = data;
    }

    public static ResponseDto<?> success() {
        return ResponseDto.builder().code("200").message("SUCCESS").build();
    }

    public static <D> ResponseDto<?> success(D data) {
        return ResponseDto.builder().code("200").message("SUCCESS").data(data).build();
    }
}
