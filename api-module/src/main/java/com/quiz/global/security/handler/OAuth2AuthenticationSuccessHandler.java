package com.quiz.global.security.handler;

import com.quiz.domain.users.entity.Users;
import com.quiz.domain.users.repository.UsersRepository;
import com.quiz.global.db.redis.Redis2Utils;
import com.quiz.global.exception.user.UserException;
import com.quiz.global.security.jwt.JwtTokenizer;
import com.quiz.global.security.userdetails.UserAccount;
import jakarta.servlet.ServletException;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.quiz.global.exception.user.code.UserErrorCode.USER_NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private final JwtTokenizer jwtTokenizer;
    private final UsersRepository usersRepository;
    private final Redis2Utils redisUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("oauth2 login success");

        try {
            OAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

            Map<String, Object> attributes = oAuth2User.getAttributes();
            String email = (String) attributes.get("email");

            Users users = usersRepository.findByEmail(email)
                    .orElseThrow(() -> new UserException(USER_NOT_FOUND));

            //create jwt token
            String accessToken = "Bearer " + jwtTokenizer.createAccessToken(email);
            String refreshToken = "Bearer " + jwtTokenizer.createRefreshToken(users.getId());

            saveAuthentication(users);

            String targetUrl = "http://localhost:3000/login";
//
//            response.addHeader(accessHeader, accessToken);
//            response.addHeader(refreshHeader, refreshToken);
            String url = UriComponentsBuilder.fromUriString(targetUrl)
                            .queryParam(accessHeader, accessToken)
                                    .queryParam(refreshHeader, refreshToken)
                                            .build().toString();

            getRedirectStrategy().sendRedirect(request, response, url);

        } catch (Exception e) {
            log.error("error : ",e);
            throw e;
        }


    }

    public void saveAuthentication(Users users) {
        UserDetails userDetails = new UserAccount(users);

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(users.getRole()));

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, roles);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
