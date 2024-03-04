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
    private boolean isAnswer;

    @Builder
    public ChoicesResponseDto(String choiceId, Long choiceIdx, Integer seq, String title, boolean isAnswer) {
        this.choiceId = choiceId;
        this.choiceIdx = choiceIdx;
        this.seq = seq;
        this.title = title;
        this.isAnswer = isAnswer;
    }
}
