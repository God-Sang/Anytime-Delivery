package com.godsang.anytimedelivery.user;


import com.godsang.anytimedelivery.category.service.CategoryService;
import com.godsang.anytimedelivery.helper.stub.StubData;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 로그인 통합 테스트
 */
@SpringBootTest(properties = "mail.address.admin=abcd@email.com")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginIntegrationTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private UserService userService;
  @MockBean
  private CategoryService categoryService;
  private String savedEmail;
  private String savedPassword;
  @Value("${mail.address.admin}")
  private String adminEmail;

  @BeforeAll
  void saveEntity() {
    User user = StubData.MockUser.getMockEntity(Role.ROLE_CUSTOMER);
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

  @Test
  @DisplayName("로그아웃 시 세션 삭제")
  void logoutTest() throws Exception {
    // given
    User user = StubData.MockUser.getMockEntity(2, adminEmail, "010-1111-1111", "관리자");
    String password = user.getPassword();
    userService.createUser(user, "customer");

    // 로그인 후 세션 얻기
    Cookie session = mockMvc.perform(
            post("/users/login")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("email", adminEmail)
                .param("password", password)
        )
        .andReturn().getResponse().getCookie("SESSION");

    doNothing().when(categoryService).deleteCategory(anyString());
    // 관리자 권한만 가능한 uri delete 요청 -> 성공
    mockMvc.perform(
            delete("/categories")
                .cookie(session)
                .param("name", "치킨")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());

    // when 로그아웃
    mockMvc.perform(
        post("/users/logout")
            .cookie(session)
            .accept(MediaType.APPLICATION_JSON)
    );

    // then 관리자 권한만 가능한 uri delete 요청 -> 실패
    mockMvc.perform(
            delete("/categories")
                .cookie(session)
                .param("name", "피자")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized());
  }
}
