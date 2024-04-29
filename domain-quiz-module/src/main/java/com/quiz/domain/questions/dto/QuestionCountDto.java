package com.quiz.domain.questions.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionCountDto {
    private Long quizId;
    private int questionCnt;
}
