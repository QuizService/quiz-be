package com.quiz.domain.participants_info.dto;

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
}
