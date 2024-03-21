package com.quiz.domain.response.dto;

import lombok.Builder;
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

    @Builder
    public ResponsesRequestDto(String questionId, Integer sequence, List<Integer> choices, String answer) {
        this.questionId = questionId;
        this.sequence = sequence;
        this.choices = choices;
        this.answer = answer;
    }
}
