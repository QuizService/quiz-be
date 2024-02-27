package com.quiz.domain.users.enums;

import com.quiz.exception.auth.AuthException;
import lombok.Getter;

import java.util.Arrays;

import static com.quiz.exception.enums.auth.AuthErrorCode.INVALID_AUTH_TYPE;

public enum AuthType {
    GOOGLE("google"),
    KAKAO("kakao");

    @Getter
    private final String provider;

    AuthType(String provider) {
        this.provider = provider;
    }

    public static AuthType findByProvider(String provider) {
        return Arrays.stream(AuthType.values())
                .filter(p -> p.provider.equals(provider))
                .findFirst()
                .orElseThrow(() -> new AuthException(INVALID_AUTH_TYPE));
    }
}
