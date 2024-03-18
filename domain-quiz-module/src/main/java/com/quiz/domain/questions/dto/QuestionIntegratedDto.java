package com.quiz.domain.questions.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionIntegratedDto {
    List<QuestionsRequestDto> questionRequestDtos;
}
