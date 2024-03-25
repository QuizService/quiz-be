package com.quiz.domain.quiz.dto;

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
