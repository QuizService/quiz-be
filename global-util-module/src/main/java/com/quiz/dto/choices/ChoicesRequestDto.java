package com.quiz.dto.choices;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChoicesRequestDto {
    private Integer sequence;
    private String title;
    private Boolean isAnswer;

    @Builder
    public ChoicesRequestDto(Integer sequence, String title, Boolean isAnswer) {
        this.sequence = sequence;
        this.title = title;
        this.isAnswer = isAnswer;
    }
}
