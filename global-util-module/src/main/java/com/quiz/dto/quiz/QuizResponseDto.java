package com.quiz.dto.quiz;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizResponseDto {
    private Long quizId;
    private String title;
    private Integer maxScore;
    private String startDate;
    private String dueDate;

    @Builder
    public QuizResponseDto(Long quizId, String title, Integer maxScore, String startDate, String dueDate) {
        this.quizId = quizId;
        this.title = title;
        this.maxScore = maxScore;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }
}
