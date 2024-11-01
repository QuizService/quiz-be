package com.quiz.domain.users.enums;

import com.quiz.enums.CodeValue;
import com.quiz.global.exception.user.UserException;
import com.quiz.global.exception.user.code.UserErrorCode;

import java.util.Arrays;

public enum AuthType implements CodeValue {
    GOOGLE("GOOGLE", "google"),
    KAKAO("KAKAO", "kakao");

    private final String code;
    private final String value;

    AuthType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static AuthType findByProvider(String provider) {
        return Arrays.stream(AuthType.values())
                .filter(p -> p.value.equals(provider))
                .findFirst()
                .orElseThrow(() -> new UserException(UserErrorCode.INVALID_AUTH_TYPE));
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
