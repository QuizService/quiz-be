package com.quiz.domain.response.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ResponsesRequestDto {
    private String questionId;
    private Integer sequence;
    private List<Integer> choices;
    private String answer;
}
