package com.quiz.domain.response.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponsesSaveDto {
    private String participantInfoId;
    private List<ResponsesRequestDto> responses;
    private Long quizId;

    @Builder
    public ResponsesSaveDto(String participantInfoId, List<ResponsesRequestDto> responses, Long quizId) {
        this.participantInfoId = participantInfoId;
        this.responses = responses;
        this.quizId = quizId;
    }
}
