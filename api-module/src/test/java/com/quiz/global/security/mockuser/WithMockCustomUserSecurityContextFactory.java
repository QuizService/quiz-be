package com.quiz.global.security.mockuser;

import com.quiz.global.security.userdetails.UserAccount;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserDetails userDetails = new UserAccount(customUser.username(), customUser.role());
        Authentication auth =
                UsernamePasswordAuthenticationToken.authenticated(userDetails, "", userDetails.getAuthorities());

        context.setAuthentication(auth);
        return context;
    }
}
