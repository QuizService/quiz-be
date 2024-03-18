package com.quiz.dto.questions;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor
public class QuestionsAnswerDto {
    private String id;
    private int sequence;
    private Integer score;
    private List<Integer> choices;
    private String answer;

    @Builder
    public QuestionsAnswerDto(String id, int sequence, Integer score, List<Integer> choices, String answer) {
        this.id = id;
        this.sequence = sequence;
        this.score = score;
        this.choices = choices;
        this.answer = answer;
    }
}
