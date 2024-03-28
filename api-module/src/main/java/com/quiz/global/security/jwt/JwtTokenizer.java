package com.quiz.global.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class JwtTokenizer {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private Algorithm jwtAlgorithm;

    @PostConstruct
    public void setJwtAlgorithm() {
        this.jwtAlgorithm = Algorithm.HMAC512(secretKey);
    }

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";

    public String createAccessToken(String email) {
        Date now = new Date();

        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT) //jwt subject 지정
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) //토큰 만료
                .withClaim(EMAIL_CLAIM, email) //payload
                .sign(jwtAlgorithm); //algorithm
    }

    public String createRefreshToken() {
        Date now = new Date();

        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(jwtAlgorithm);
    }

    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
        log.info("accessToken : {}", accessToken);
    }

    public void sendRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
        log.info("refreshToken : {}", refreshToken);
    }

    public Optional<String> extractEmail(HttpServletRequest request) {
        Optional<String> accessToken = extractAccessToken(request);
        try {
            if(accessToken.isPresent()) {
                String token = accessToken.get();
                String optionalEmail =  JWT.require(Algorithm.HMAC512(secretKey))
                        .build()
                        .verify(token)
                        .getClaim(EMAIL_CLAIM)
                        .asString();
                return Optional.of(optionalEmail);
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("jwt not valid", e);
            throw new IllegalArgumentException(e);
        }

    }

    public String getEmail(String accessToken) {
        return JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(accessToken)
                .getClaim(EMAIL_CLAIM)
                .asString();
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(at -> at.startsWith(BEARER))
                .map(at -> at.replace(BEARER, ""));
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(at -> at.startsWith(BEARER))
                .map(at -> at.replace(BEARER, ""));
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(jwtAlgorithm)
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            log.error("error : {}", e.getMessage());
            return false;
        }
    }




}
