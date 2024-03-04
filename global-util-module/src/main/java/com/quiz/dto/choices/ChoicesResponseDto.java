package com.quiz.dto.choices;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChoicesResponseDto {
    private String choiceId;
    private Long choiceIdx;
    private Integer seq;
    private String title;

    @Builder
    public ChoicesResponseDto(String choiceId, Long choiceIdx, Integer seq, String title) {
        this.choiceId = choiceId;
        this.choiceIdx = choiceIdx;
        this.seq = seq;
        this.title = title;
    }
}
