package com.quiz.domain.questions.dto;

import com.quiz.domain.choice.dto.ChoicesResponseAdminDto;
import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionsResponseAdminDto {
    private String questionId;
    private String title;
    private Integer score;
    private String questionType;
    private List<ChoicesResponseAdminDto> choicesResponseDtos;
    private String answer;

    @Builder
    public QuestionsResponseAdminDto(String questionId, String title, Integer score, String questionType, List<ChoicesResponseAdminDto> choicesResponseDtos, String answer) {
        this.questionId = questionId;
        this.title = title;
        this.score = score;
        this.questionType = questionType;
        this.choicesResponseDtos = choicesResponseDtos;
        this.answer = answer;
    }
}
