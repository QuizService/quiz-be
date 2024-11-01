package com.quiz.exception.code;

public enum ParticipantInfoErrorCode implements ErrorCode {
    PARTICIPANT_IS_NOT_IN_QUIZ("participant not in quiz", 400), ALREADY_PARTICIPATED("already participated", 400), FIRST_COME_FIRST_SERVED_END("first come first served end", 400), PARTICIPANT_NOT_FOUND("participant not found", 404), START_DATE_IS_NOT_NOW("startDate is not now", 400);

    private final String message;
    private final int code;

    ParticipantInfoErrorCode(String message, int code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
