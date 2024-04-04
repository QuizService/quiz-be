package com.quiz.domain.quiz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "quiz 메타정보")
@Getter
public class QuizRequestDto {
    @NotBlank
    private String title;
    @Min(-1)
    @Schema(description = "선착순 인원, 선착순 미적용시 -1 입력")
    private Integer capacity;
    @Schema(description = "시작일 yyyy-MM-dd hh:mm:ss")
    private String startDate;
    @Schema(description = "마감일 yyyy-MM-dd hh:mm:ss")
    private String dueDate;

    @Builder
    public QuizRequestDto(String title, Integer capacity, String startDate, String dueDate) {
        this.title = title;
        this.capacity = capacity;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }
}
