package com.quiz.domain.quiz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Quiz 조회 response")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizResponseDto {
    @Schema(description = "Quiz id")
    private Long quizId;
    private String title;
    private int capacity;
    @Schema(description = "Quiz 총 점수")
    private Integer maxScore;
    @Schema(description = "Quiz 시작일")
    private String startDate;
    @Schema(description = "Quiz 마감일")
    private String dueDate;
    private Boolean isQuestionsCreated;
    private String created;

    @Builder
    public QuizResponseDto(Long quizId, String title, int capacity, Integer maxScore, String startDate, String dueDate, Boolean isQuestionsCreated, String created) {
        this.quizId = quizId;
        this.title = title;
        this.capacity = capacity;
        this.maxScore = maxScore;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.isQuestionsCreated = isQuestionsCreated;
        this.created = created;
    }
}
