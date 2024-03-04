package com.quiz.api_module.com.quiz.global.security.config;

import com.quiz.api_module.com.quiz.global.security.filter.JwtAuthorizationProcessingFilter;
import com.quiz.api_module.com.quiz.global.security.handler.OAuth2AuthenticationFailureHandler;
import com.quiz.api_module.com.quiz.global.security.handler.OAuth2AuthenticationSuccessHandler;
import com.quiz.api_module.com.quiz.global.security.oauth.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    private final JwtAuthorizationProcessingFilter jwtAuthorizationProcessingFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2/**").permitAll()
                        .requestMatchers("/css/**","/images/**","/js/**","/favicon.ico").permitAll()
                        .requestMatchers("/","/login","/api/v1/**","/index.html").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/**", "/swagger-ui.html").permitAll()
                        .anyRequest().hasRole("USER"))
                .addFilterBefore(jwtAuthorizationProcessingFilter, UsernamePasswordAuthenticationFilter.class);
        http
                .oauth2Login(oauth2 ->
                        oauth2.successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)
                                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService)))
                //jwt token 인증

                .logout(logout -> logout
                        .permitAll()
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .addLogoutHandler(new SecurityContextLogoutHandler()));
        return http.build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost:3000"));
        configuration.setAllowedMethods(List.of("POST", "PUT", "PATCH", "GET", "DELETE", "OPTIONS"));
        configuration.addAllowedHeader("*"); // 모든 헤더 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


}
