package com.quiz.domain.participants_info.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParticipantsRankResponseDto {
    private String id;
    private Long userId;
    private String username;
    private Integer number;
    private Integer score;

    @Builder
    public ParticipantsRankResponseDto(String id, Long userId, String username, Integer number, Integer score) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.number = number;
        this.score = score;
    }
}
