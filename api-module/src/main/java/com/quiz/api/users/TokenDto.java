package com.quiz.api.users;


import lombok.*;

public record TokenDto(String accessToken, String refreshToken) {

    @Builder
    public TokenDto {
    }
}
