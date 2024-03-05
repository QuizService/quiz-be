package com.quiz.dto.questions;

import com.quiz.dto.choices.ChoicesRequestDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Validated
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionsRequestDto {
    private String questionId;
    @NotBlank
    private String title;
    @NotBlank
    private Integer score;
    private Integer sequence;
    @NotBlank
    private String questionType;
    private List<ChoicesRequestDto> choices;
    private String answer;

    @Builder
    public QuestionsRequestDto(String questionId, String title, Integer score, Integer sequence, String questionType, List<ChoicesRequestDto> choices, String answer) {
        this.questionId = questionId;
        this.title = title;
        this.score = score;
        this.sequence = sequence;
        this.questionType = questionType;
        this.choices = choices;
        this.answer = answer;
    }
}
