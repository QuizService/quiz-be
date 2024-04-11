package com.quiz.global.queue;

public record ParticipantQueueInfoDto(Long quizId, Long userId, boolean isCapacityLeft, boolean isUsersTurn) {
}
