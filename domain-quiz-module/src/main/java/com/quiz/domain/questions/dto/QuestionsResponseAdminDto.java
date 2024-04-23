package com.quiz.domain.questions.dto;

import com.quiz.domain.choice.dto.ChoicesResponseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionsResponseAdminDto {
    private String questionId;
    private String title;
    private Integer score;
    private String questionType;
    private List<ChoicesResponseDto> choicesResponseDtos;
    private List<Integer> multipleChoiceAnswer;
    private String shortAnswer;

    @Builder
    public QuestionsResponseAdminDto(String questionId, String title, Integer score, String questionType, List<ChoicesResponseDto> choicesResponseDtos, List<Integer> multipleChoiceAnswer, String shortAnswer) {
        this.questionId = questionId;
        this.title = title;
        this.score = score;
        this.questionType = questionType;
        this.choicesResponseDtos = choicesResponseDtos;
        this.multipleChoiceAnswer = multipleChoiceAnswer;
        this.shortAnswer = shortAnswer;
    }
}
