package com.godsang.anytimedelivery.user;


import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.user.entity.Role;
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

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginIntegrationTest {
  private final String savedEmail = "anytime@email.com";
  private final String savedPassword = "1q2w3e4r@";
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private UserService userService;

  @BeforeAll
  void saveEntity() {
    User user = StubData.MockUser.getMockEntity(1L, savedEmail, savedPassword, "010-1234-5678", "애니타임", Role.ROLE_OWNER);
    userService.createUser(user, "owner");
  }

  @Test
  @DisplayName("로그인 성공 후 세션 생성")
  void loginSuccessTest() throws Exception {
    mockMvc.perform(
            post("/users/login")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("email", savedEmail)
                .param("password", savedPassword)
        )
        .andExpect(status().isOk())
        .andExpect(result -> assertThat(result.getResponse().getCookie("SESSION")).isNotNull());
  }

  @Test
  @DisplayName("로그인 실패")
  void loginFailureTest() throws Exception {
    String invalidEmail = "asdf@gmail.com";

    mockMvc.perform(
            post("/users/login")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("email", invalidEmail)
                .param("password", savedPassword)
        )
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("Login failed."));
  }
}
