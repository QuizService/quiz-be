package com.quiz.global.security.filter;

import com.quiz.domain.users.entity.Users;
import com.quiz.domain.users.repository.UsersRepository;
import com.quiz.global.db.redis.Redis2Utils;
import com.quiz.exception.UserException;
import com.quiz.exception.AuthException;
import com.quiz.global.security.jwt.JwtTokenizer;
import com.quiz.global.security.userdetails.UserAccount;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.quiz.exception.code.UserErrorCode.USER_NOT_FOUND;
import static com.quiz.exception.code.AuthErrorCode.JWT_NOT_VALID;
import static com.quiz.exception.code.AuthErrorCode.REFRESH_TOKEN_NOT_EXIST;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationProcessingFilter extends OncePerRequestFilter {
    private static final String[] AUTHORIZATION_NOT_REQUIRED = new String[]{"/login", "/favicon.ico", "/h2", "/favicon.ico", "/index.html", "/web-socket-connection", "/swagger-ui", "/v3/api-docs", "/topic/participant", "/api/login", "/api/googleLogin", "/health-check"};
    private final JwtTokenizer jwtTokenizer;
    private final UsersRepository usersRepository;
    private final Redis2Utils redisUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.startsWithAny(request.getRequestURI(), AUTHORIZATION_NOT_REQUIRED)) {
            filterChain.doFilter(request, response);
            log.info("AUTHORIZATION_NOT_REQUIRED");
            return;
        }
        //accessToken 확인
        Optional<String> accessToken = jwtTokenizer.extractAccessToken(request);
        if (accessToken.isPresent()) {
            // 만약 accessToken 존재시 return;
            boolean isAccessTokenValid = jwtTokenizer.isTokenValid(accessToken.get());
            if (isAccessTokenValid) {
                setAuthentication(accessToken.get());
            }
        } else {
            log.info("accessToken not valid");
            Optional<String> refreshToken = jwtTokenizer.extractRefreshToken(request);
            //refresh token 존재시 accessToken reissue 후 return;
            if (refreshToken.isPresent()) {
                //refreshToken valid check
                checkRefreshToken(response, refreshToken.get());
            } else {
                log.info("refresh token not exist");
                throw new AuthException(REFRESH_TOKEN_NOT_EXIST);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void checkRefreshToken(HttpServletResponse response, String refreshToken) {
        if (!jwtTokenizer.isTokenValid(refreshToken)) {
            log.info("refresh token not valid");
            throw new AuthException(JWT_NOT_VALID);
        }
        Long userId = redisUtils.getObject(refreshToken)
                .orElseThrow(() -> new AuthException(JWT_NOT_VALID));
        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
        reIssueToken(users, response, refreshToken);
    }

    private void reIssueToken(Users users, HttpServletResponse response, String refreshToken) {
        String token = jwtTokenizer.createRefreshToken(users.getId(), refreshToken);
        String accessToken = jwtTokenizer.createAccessToken(users.getEmail());

        //securityContext에 저장
        saveAuthentication(users);
        //response에 저장
        jwtTokenizer.sendAccessToken(response, accessToken);
        jwtTokenizer.sendRefreshToken(response, token);
    }

    private void setAuthentication(String accessToken) {
        Users users = getUsers(accessToken);
        saveAuthentication(users);
    }

    private Users getUsers(String accessToken) {
        String email = jwtTokenizer.getEmail(accessToken);
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    private void saveAuthentication(Users users) {
        UserDetails userDetails = new UserAccount(users);

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(users.getRole()));

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, roles);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return StringUtils.startsWithAny(request.getRequestURI(), AUTHORIZATION_NOT_REQUIRED);
    }
}
