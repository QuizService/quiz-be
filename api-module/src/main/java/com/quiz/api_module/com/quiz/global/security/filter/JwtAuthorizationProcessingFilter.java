package com.quiz.api_module.com.quiz.global.security.filter;

import com.quiz.domain.users.entity.Users;
import com.quiz.domain.users.repository.UsersRepository;
import com.quiz.api_module.com.quiz.global.security.jwt.JwtTokenizer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationProcessingFilter extends OncePerRequestFilter {
    private static final List<String> AUTHORIZATION_NOT_REQUIRED = List.of("/login","/","/favicon.","/h2/**", "/favicon.ico","/hello","/index.html");
    private final JwtTokenizer jwtTokenizer;
    private final UsersRepository usersRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtAuthorizationProcessingFilter start");
        log.info("request.getRequestURI() = {}",request.getRequestURI());
        if(AUTHORIZATION_NOT_REQUIRED.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            log.info("AUTHORIZATION_NOT_REQUIRED");
            return;
        }
        //accessToken 확인
        Optional<String> accessToken = jwtTokenizer.extractAccessToken(request);
        if(accessToken.isPresent()) {
            // 만약 accessToken 존재시 return;
            log.info("accessToken exist");
            boolean isAccessTokenValid = jwtTokenizer.isTokenValid(accessToken.get());
            if(isAccessTokenValid) {
                log.info("accessToken valid");
                return;
            }
        }
        //없다면 refreshToken 존재 확인
        Optional<String> refreshToken = jwtTokenizer.extractRefreshToken(request);
        //refresh token 존재시 accessToken reissue 후 return;
        if(refreshToken.isPresent()) {
            boolean isRefreshTokenValid = jwtTokenizer.isTokenValid(refreshToken.get());
            //refreshToken 유효하지 않으면 그냥 리턴 -> dofilter 로직으로
            if(!isRefreshTokenValid) {
                log.info("refresh token not valid");
                filterChain.doFilter(request, response);
            }
            checkRefreshTokenAndReIssueAccessToken(refreshToken.get(), response);
            return;
        }
        log.info("refresh token not exist");
        filterChain.doFilter(request, response);
    }

    public void checkRefreshTokenAndReIssueAccessToken(String refreshToken, HttpServletResponse response) {
        log.info("checkRefreshTokenAndReIssueAccessToken start");
        //refreshToken으로 user 찾은 후 accessToken 재 발급
        usersRepository.findByRefreshToken(refreshToken)
                .ifPresent(users -> {
                    String token = reissueRefreshToken(users);
                    String accessToken = jwtTokenizer.createAccessToken(users.getEmail());
                    //refreshToken 다시 저장
                    users.setRefreshToken(token);
                    users = usersRepository.saveAndFlush(users);

                    //securityContext에 저장
                    saveAuthentication(users);
                    //response에 저장
                    jwtTokenizer.sendAccessToken(response, accessToken);
                    jwtTokenizer.sendRefreshToken(response, token);
                });
        log.info("checkRefreshTokenAndReIssueAccessToken end");

    }

    private String reissueRefreshToken(Users users) {
        String refreshToken = jwtTokenizer.createRefreshToken();
        users.setRefreshToken(refreshToken);
        usersRepository.saveAndFlush(users);

        return refreshToken;
    }

    public void saveAuthentication(Users users) {
        UserDetails userDetails = User.builder()
                .username(users.getName())
                .roles(users.getRole())
                .build();

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(users.getRole()));

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, roles);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        log.info("should not filter : request url = {}", request.getRequestURI());
        return AUTHORIZATION_NOT_REQUIRED.contains(request.getRequestURI());
    }
}
