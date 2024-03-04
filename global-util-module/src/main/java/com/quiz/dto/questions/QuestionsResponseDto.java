package com.quiz.dto.questions;


import com.quiz.dto.answers.AnswerResponseDto;
import com.quiz.dto.choices.ChoicesResponseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionsResponseDto {
    private Long questionId;
    private String title;
    private Integer score;
    private String questionType;
    private List<ChoicesResponseDto> choicesResponseDtos;
    private AnswerResponseDto answerResponseDto;

    @Builder
    public QuestionsResponseDto(Long questionId, String title, Integer score, String questionType, List<ChoicesResponseDto> choicesResponseDtos, AnswerResponseDto answerResponseDto) {
        this.questionId = questionId;
        this.title = title;
        this.score = score;
        this.questionType = questionType;
        this.choicesResponseDtos = choicesResponseDtos;
        this.answerResponseDto = answerResponseDto;
    }
}
