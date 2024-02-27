package com.quiz.dto.quiz;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class QuizRequestDto {
    private String title;
    private Integer capacity;
    private String startDate;
    private String dueDate;

    @Builder
    public QuizRequestDto(String title, Integer capacity, String startDate, String dueDate) {
        this.title = title;
        this.capacity = capacity;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }
}
