package com.quiz.global.security.dto;


import lombok.*;

public record TokenDto(String accessToken, String refreshToken) {

    @Builder
    public TokenDto {
    }
}
