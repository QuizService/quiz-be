package com.quiz.global.queue;

public record ParticipantQueueInfoDto(Long quizId, Long userId, Long rank, boolean isCapacityLeft) {
}
