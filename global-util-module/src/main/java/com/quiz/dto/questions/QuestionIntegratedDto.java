package com.quiz.dto.questions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionIntegratedDto {
    List<QuestionsRequestDto> questionRequestDtos;
}
