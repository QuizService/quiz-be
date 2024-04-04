package com.quiz.domain.questions.dto;

import com.quiz.domain.choice.dto.ChoicesRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Validated
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionsRequestDto {
    private String questionId;
    @NotBlank
    @Schema(description = "question 제목")
    private String title;
    @Min(0)
    @Schema(description = "question 점수")
    private Integer score;
    @Min(0)
    @Schema(description = "question 문항번호")
    private Integer sequence;
    @NotBlank
    @Schema(description = "question 문제 유형, 객관식 = M, 단답형 = S")
    private String questionType;
    @Schema(description = "객관식 문항(choice) list, 단답형인 경우 빈 리스트")
    private List<ChoicesRequestDto> choices;
    @Schema(description = "단답형 정답, 객관식인 경우 null")
    private String answer;

    @Builder
    public QuestionsRequestDto(String questionId, String title, Integer score, Integer sequence, String questionType, List<ChoicesRequestDto> choices, String answer) {
        this.questionId = questionId;
        this.title = title;
        this.score = score;
        this.sequence = sequence;
        this.questionType = questionType;
        this.choices = choices;
        this.answer = answer;
    }
}
