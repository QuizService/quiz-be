package com.quiz.domain.participants_info.dto;

import lombok.Builder;

public record ParticipantQueueDto(Long quizId, Long userId) {

    @Builder
    public ParticipantQueueDto {
    }
}
