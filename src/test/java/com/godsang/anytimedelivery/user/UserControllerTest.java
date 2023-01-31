package com.godsang.anytimedelivery.user;

import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.user.controller.UserController;
import com.godsang.anytimedelivery.user.dto.UserDto;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.mapper.UserMapper;
import com.godsang.anytimedelivery.user.service.UserService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
@ActiveProfiles("signupTest")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;
    @Autowired
    private Gson gson;
    private String email;
    private String phone;
    private String nickname;
    private String password;
    private String role;

    @BeforeEach
    void assignValue() {
        email = "delivery@email.com";
        phone = "010-1234-5678";
        nickname = "애니타임";
        password = "1q2w3e4r@";
        role = Role.ROLE_OWNER.getName();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signupSuccessTest() throws Exception {
        // given
        UserDto.Post postDto = StubData.MockUserDto.getMockPost(email, password, phone, nickname, role);
        String content = gson.toJson(postDto);

        given(userMapper.userToResponse(Mockito.any())).willReturn(StubData.MockUserDto.getMockResponse(email, phone, nickname, Role.ROLE_OWNER));

        // when, then
        mockMvc.perform(
                        post("/users/signup")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.data.phone").value(phone))
                .andExpect(jsonPath("$.data.nickName").value(nickname))
                .andExpect(jsonPath("$.data.role").value(String.valueOf(Role.ROLE_OWNER)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"google.com", "hello#naver.com", "hello@navercom", "abcgoogle", "ABC@GOOGLE.COM"})
    @DisplayName("이메일 형식이 맞지 않음")
    void invalidEmailTest(String invalidEmail) throws Exception {
        // given
        email = invalidEmail;
        UserDto.Post postDto = StubData.MockUserDto.getMockPost(email, password, phone, nickname, role);
        String content = gson.toJson(postDto);

        // when, then
        mockMvc.perform(
                        post("/users/signup")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"가나다라123@", "1q2w3e4r", "helloworld$#", "a1@2b"})
    @DisplayName("비밀번호 형식이 맞지 않음")
    void invalidPasswordTest(String invalidPassword) throws Exception {
        // given
        password = invalidPassword;
        UserDto.Post postDto = StubData.MockUserDto.getMockPost(email, password, phone, nickname, role);
        String content = gson.toJson(postDto);

        // when, then
        mockMvc.perform(
                        post("/users/signup")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"010-12345678", "0111-123-4567", "010-12345-1234", "010-123-45678", "010_3333-3333"})
    @DisplayName("핸드폰 번호 형식이 맞지 않음")
    void invalidPhoneNumberTest(String invalidPhone) throws Exception {
        // given
        phone = invalidPhone;
        UserDto.Post postDto = StubData.MockUserDto.getMockPost(email, password, phone, nickname, role);
        String content = gson.toJson(postDto);

        // when, then
        mockMvc.perform(
                        post("/users/signup")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"anytimeDelivery", "닉네임@!", "삼", "1341234", "dev "})
    @DisplayName("닉네임 형식이 맞지 않음")
    void invalidNickNameTest(String invalidNickName) throws Exception {
        // given
        nickname = invalidNickName;
        UserDto.Post postDto = StubData.MockUserDto.getMockPost(email, password, phone, nickname, role);
        String content = gson.toJson(postDto);

        // when, then
        mockMvc.perform(
                        post("/users/signup")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isBadRequest());
    }
}
