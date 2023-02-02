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

  @BeforeAll
  void saveEntity() {
    User user = StubData.MockUser.getMockEntity(1L, "anytime@email.com", "1q2w3e4r@", "010-1234-5678", "애니타임", Role.ROLE_CUSTOMER);
    userRepository.save(user);
  }

  @Test
  @DisplayName("회원가입 성공: response, role, password encoding 확인")
  void signupTest() throws Exception {
    // given
    String email = "asdf@email.com";
    UserDto.Post postDto = StubData.MockUserDto.getMockPost(email, "1q2w3e4r@", "010-1111-2222", "고길동", Role.ROLE_CUSTOMER.getName());
    String content = gson.toJson(postDto);

    // when, then
    mockMvc.perform(
            post("/users/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
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
    String email = "anytime@email.com";
    UserDto.Post postDto = StubData.MockUserDto.getMockPost(email, "1q2w3e4r@", "010-5656-5656", "곽두팔", Role.ROLE_CUSTOMER.getName());
    String content = gson.toJson(postDto);

    // when, then
    mockMvc.perform(
            post("/users/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value(ExceptionCode.EMAIL_ALREADY_EXISTS.getDescription()));

  }

  @Test
  @DisplayName("회원가입 실패: 중복된 핸드폰 번호")
  void duplicatedPhoneTest() throws Exception {
    // given
    String phone = "010-1234-5678";
    UserDto.Post postDto = StubData.MockUserDto.getMockPost("hello@email.com", "1q2w3e4r@", phone, "박춘삼", Role.ROLE_CUSTOMER.getName());
    String content = gson.toJson(postDto);

    // when, then
    mockMvc.perform(
            post("/users/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value(ExceptionCode.PHONE_NUMBER_ALREADY_EXISTS.getDescription()));
  }

  @Test
  @DisplayName("회원가입 실패: 중복된 닉네임")
  void duplicatedNickNameTest() throws Exception {
    // given
    String nickName = "애니타임";
    UserDto.Post postDto = StubData.MockUserDto.getMockPost("newjeans@email.com", "1q2w3e4r@", "010-1333-9999", nickName, Role.ROLE_CUSTOMER.getName());
    String content = gson.toJson(postDto);

    // when, then
    mockMvc.perform(
            post("/users/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value(ExceptionCode.NICKNAME_ALREADY_EXISTS.getDescription()));
  }
}
