package com.quiz.dto.answers;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerResponseDto {
    private String answerId;
    private Long answerIdx;
}
