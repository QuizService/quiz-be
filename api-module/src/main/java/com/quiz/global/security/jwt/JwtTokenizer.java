package com.quiz.global.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.quiz.global.db.redis.Redis2Utils;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
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

    private final Redis2Utils redisUtils;

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

    public String createRefreshToken(Long userId, String rt) {
        Date now = new Date();

        // 기존 refresh token 삭제
        redisUtils.deleteObject(rt);
        String refreshToken = JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(jwtAlgorithm);
        redisUtils.addObject(refreshToken, userId.toString(), refreshTokenExpirationPeriod);

        return refreshToken;
    }

    public String createRefreshTokenWhenLogin(Long userId) {
        Date now = new Date();

        String refreshToken = JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(jwtAlgorithm);
        redisUtils.addObject(refreshToken, userId.toString(), refreshTokenExpirationPeriod);

        return refreshToken;
    }

    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
        log.info("set accessToken to header");
    }

    public void sendRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
        log.info("set refreshToken to header");
    }

    public Optional<String> extractEmail(HttpServletRequest request) {
        Optional<String> accessToken = extractAccessToken(request);
        try {
            if (accessToken.isPresent()) {
                String token = accessToken.get();
                String optionalEmail = JWT.require(Algorithm.HMAC512(secretKey))
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
        DecodedJWT jwt = JWT.decode(accessToken);
        return jwt.getClaim(EMAIL_CLAIM).asString();
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
        } catch (TokenExpiredException e) {
            log.error("token expired : {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("jwt error : {}", e.getMessage());
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        DecodedJWT jwt = JWT.decode(token);
        Date expDate = jwt.getExpiresAt();
        Date now = new Date();

        return now.after(expDate);
    }
}
