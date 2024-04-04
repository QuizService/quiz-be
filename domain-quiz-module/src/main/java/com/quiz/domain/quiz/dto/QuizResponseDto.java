package com.quiz.domain.quiz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@ToString
@Schema(description = "Quiz 조회 response")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizResponseDto {
    @Schema(description = "Quiz id")
    private Long quizId;
    private String title;
    @Schema(description = "Quiz 총 점수")
    private Integer maxScore;
    @Schema(description = "Quiz 시작일")
    private String startDate;
    @Schema(description = "Quiz 마감일")
    private String dueDate;
    private String created;

    @Builder
    public QuizResponseDto(Long quizId, String title, Integer maxScore, String startDate, String dueDate, String created) {
        this.quizId = quizId;
        this.title = title;
        this.maxScore = maxScore;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.created = created;
    }
}
