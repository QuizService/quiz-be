package com.quiz.domain.choice.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChoicesResponseAdminDto {
    private Integer seq;
    private String title;
    private boolean isAnswer;

    @Builder
    public ChoicesResponseAdminDto(Integer seq, String title, boolean isAnswer) {
        this.seq = seq;
        this.title = title;
        this.isAnswer = isAnswer;
    }
}
