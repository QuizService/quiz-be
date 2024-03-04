package com.quiz.api_module.com.quiz.global.security.oauth;

import com.quiz.domain.users.entity.Users;
import com.quiz.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UsersRepository usersRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("customOAuth2UserService.loadUser() start");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        OAuthAttribute oAuthAttribute = OAuthAttribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Users users = saveOrUpdate(oAuthAttribute);

        log.info("CustomOAuth2UserService.loadUser() end");
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(users.getRole())),
                oAuthAttribute.getAttributes(),
                oAuthAttribute.getNameAttributeKey());
    }

    private Users saveOrUpdate(OAuthAttribute oAuthAttribute) {
        String email = oAuthAttribute.getEmail();
        Optional<Users> users = usersRepository.findByEmail(email);
        if(users.isEmpty()) {
            Users newUser = oAuthAttribute.toEntity();
            return usersRepository.save(newUser);
        }
        Users users1 = users.get();
        users1.update(oAuthAttribute.getName(), oAuthAttribute.getPicture());
        return usersRepository.save(users1);
    }
}
