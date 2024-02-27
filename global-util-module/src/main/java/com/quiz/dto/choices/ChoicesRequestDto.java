package com.quiz.dto.choices;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChoicesRequestDto {
    private Integer sequence;
    private String title;
    private Boolean isAnswer;
}
