package com.quiz.dto.questions;

import com.quiz.dto.choices.ChoicesRequestDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Validated
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionsRequestDto {
    private Long questionId;
    @NotBlank
    private String title;
    @NotBlank
    private Integer score;
    private Integer sequence;
    @NotBlank
    private String questionType;
    private List<ChoicesRequestDto> choices;
    private String answer;
}
