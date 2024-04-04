package com.quiz.domain.questions.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "questions requestDto list")
public class QuestionIntegratedDto {
    List<QuestionsRequestDto> questionRequestDtos;
}
