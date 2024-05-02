package com.quiz.domain.users.dto;

import lombok.Builder;

public record UserInfoDto(String name, String email, String picture) {
    @Builder
    public UserInfoDto {
    }
}
