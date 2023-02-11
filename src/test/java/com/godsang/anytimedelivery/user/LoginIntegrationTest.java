package com.godsang.anytimedelivery.user;


import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 로그인 통합 테스트
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginIntegrationTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private UserService userService;
  private String savedEmail;
  private String savedPassword;

  @BeforeAll
  void saveEntity() {
    User user = StubData.MockUser.builder()
        .userId(1L)
        .email("anytime123@gmail.com")
        .phone("010-1234-5678")
        .nickName("애니타임")
        .build();
    savedEmail = user.getEmail();
    savedPassword = user.getPassword();

    // encoding된 비밀번호를 저장하기위해 UserService의 메서드 호출
    userService.createUser(user, "owner");
  }

  @Test
  @DisplayName("로그인 성공 후 세션 생성")
  void loginSuccessTest() throws Exception {
    // when
    mockMvc.perform(
            post("/users/login")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("email", savedEmail)
                .param("password", savedPassword)
        )
        // then
        .andExpect(status().isOk())
        .andExpect(result -> assertThat(result.getResponse().getCookie("SESSION")).isNotNull());
  }

  @Test
  @DisplayName("로그인 실패: DB에 없는 이메일")
  void loginFailureTest1() throws Exception {
    // given
    String invalidEmail = "asdf@gmail.com";

    // when
    mockMvc.perform(
            post("/users/login")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("email", invalidEmail)
                .param("password", savedPassword)
        )
        // then
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("Login failed."));
  }

  @Test
  @DisplayName("로그인 실패: 비밀번호가 맞지 않음")
  void loginFailureTest2() throws Exception {
    // given
    String invalidPassword = "asdfasdfasfd22@#";

    // when
    mockMvc.perform(
            post("/users/login")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("email", savedEmail)
                .param("password", invalidPassword)
        )
        // then
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("Login failed."));
  }
}
