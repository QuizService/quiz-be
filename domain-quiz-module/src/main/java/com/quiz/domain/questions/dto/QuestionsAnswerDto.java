package com.quiz.domain.questions.dto;

import com.quiz.domain.questions.entity.QuestionType;
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
    private QuestionType questionType;
    private List<Integer> choices;
    private String answer;

    @Builder
    public QuestionsAnswerDto(String id, int sequence, Integer score, QuestionType questionType, List<Integer> choices, String answer) {
        this.id = id;
        this.sequence = sequence;
        this.score = score;
        this.questionType = questionType;
        this.choices = choices;
        this.answer = answer;
    }
}
