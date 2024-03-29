package com.quiz.domain.choice.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChoicesResponseDto {
    private Integer seq;
    private String title;

    @Builder
    public ChoicesResponseDto(Integer seq, String title) {
        this.seq = seq;
        this.title = title;
    }
}
