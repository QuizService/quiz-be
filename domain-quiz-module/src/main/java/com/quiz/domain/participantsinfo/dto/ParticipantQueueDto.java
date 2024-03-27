package com.quiz.domain.participantsinfo.dto;

import lombok.Builder;

public record ParticipantQueueDto(Long quizId, Long userId) {

    @Builder
    public ParticipantQueueDto {
    }
}
