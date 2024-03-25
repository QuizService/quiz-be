package com.quiz.domain.response.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponsesRequestsDto {
    private List<ResponsesRequestDto> responses;
}
