package com.quiz.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ResponsesRequestDto {
    private String questionId;
    private List<Integer> choices;
    private String answer;
}
