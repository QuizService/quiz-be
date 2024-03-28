package com.quiz.api.test;

import com.quiz.domain.users.repository.UsersRepository;
import com.quiz.global.security.filter.JwtAuthorizationProcessingFilter;
import com.quiz.global.security.jwt.JwtTokenizer;
import com.quiz.global.security.mockuser.WithMockCustomUser;
import com.quiz.global.security.oauth.CustomOAuth2UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TestController.class) //test 할 컨트롤러
@MockBeans({
        @MockBean(JpaMetamodelMappingContext.class),
        @MockBean(CustomOAuth2UserService.class),
        @MockBean(JwtAuthorizationProcessingFilter.class),
        @MockBean(JwtTokenizer.class),
        @MockBean(UsersRepository.class),
})
public class TestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ClientRegistrationRepository clientRegistrationRepository;

    @Test
    @WithMockCustomUser
    void test() throws Exception {
        mvc.perform(get("/api/v1/hello"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
