package com.quiz.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResultDto {
    private String id;
    private Long userId;
    private String username;
    private Integer number;
    private Integer score;
}
