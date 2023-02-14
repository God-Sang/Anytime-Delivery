package com.godsang.anytimedelivery.user;

import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.user.dto.UserDto;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.repository.UserRepository;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 회원가입 통합 테스트
 * 메서드마다 rollback 하기 위해 @Transactional 적용
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class SignupIntegrationTest {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private Gson gson;
  @Autowired
  private MockMvc mockMvc;
  private String savedEmail;
  private String savedPhone;
  private String savedNickName;

  @BeforeAll
  void saveEntity() {
    User user = StubData.MockUser.getMockEntity(Role.ROLE_CUSTOMER);
    userRepository.save(user);
    savedEmail = user.getEmail();
    savedPhone = user.getPhone();
    savedNickName = user.getNickName();
  }

  @Test
  @DisplayName("회원가입 성공: 성공 후 email, role, password encoding 확인")
  void signupTest() throws Exception {
    // given
    String email = "asdf@email.com";
    String phone = "010-1111-2222";
    String nickName = "고길동";
    String content = gson.toJson(getPostDto(email, phone, nickName));

    // when
    mockMvc.perform(
            post("/users/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.email").value(email));

    User findUser = userRepository.findByEmail(email).get();
    assertThat(findUser.getRole()).isEqualTo(Role.ROLE_CUSTOMER);
    assertThat(findUser.getPassword()).contains("{bcrypt}");
  }

  @Test
  @DisplayName("회원가입 실패: 중복된 이메일")
  void duplicatedEmailTest() throws Exception {
    // given
    String phone = "010-1111-2222";
    String nickName = "고길동";
    String content = gson.toJson(getPostDto(savedEmail, phone, nickName));

    // when
    mockMvc.perform(
            post("/users/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value(ExceptionCode.EMAIL_ALREADY_EXISTS.getDescription()));

  }

  @Test
  @DisplayName("회원가입 실패: 중복된 핸드폰 번호")
  void duplicatedPhoneTest() throws Exception {
    // given
    String email = "asdf@email.com";
    String nickName = "고길동";
    String content = gson.toJson(getPostDto(email, savedPhone, nickName));

    // when
    mockMvc.perform(
            post("/users/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value(ExceptionCode.PHONE_NUMBER_ALREADY_EXISTS.getDescription()));
  }

  @Test
  @DisplayName("회원가입 실패: 중복된 닉네임")
  void duplicatedNickNameTest() throws Exception {
    // given
    String email = "asdf@email.com";
    String phone = "010-1111-2222";
    String content = gson.toJson(getPostDto(email, phone, savedNickName));

    // when
    mockMvc.perform(
            post("/users/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value(ExceptionCode.NICKNAME_ALREADY_EXISTS.getDescription()));
  }

  private UserDto.Post getPostDto(String email, String phone, String nickName) {
    return StubData.MockUserPost.builder()
        .email(email)
        .password("1q2w3e4r@")
        .phone(phone)
        .nickName(nickName)
        .build();
  }
}
