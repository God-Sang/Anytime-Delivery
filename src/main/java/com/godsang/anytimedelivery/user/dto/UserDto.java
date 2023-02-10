package com.godsang.anytimedelivery.user.dto;

import com.godsang.anytimedelivery.user.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserDto {
  @Getter
  @Setter
  @NoArgsConstructor
  public static class Post {
    @NotBlank
    @Pattern(regexp = "[a-z\\d]+@[a-z]+\\.[a-z]+(\\.[a-z]+)?", message = "올바르지 않은 이메일입니다.")
    private String email;
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[$@!%*?&#~])[A-Za-z\\d$@!%*?&#~]{8,15}", message = "영어, 숫자, 특수문자를 포함하여 8 ~ 15 글자 이내여야 합니다.")
    private String password;
    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "올바르지 않은 핸드폰 번호입니다.")
    private String phone;
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z가-힣])[0-9a-zA-Z가-힣]{2,10}$", message = "2 ~ 10 글자 이내의 한글, 영어, 숫자만 가능하며 한글, 영어는 반드시 하나 이상 포함되어야 합니다.")
    private String nickName;
    @NotBlank
    private String role;

    @Builder
    public Post(String email, String password, String phone, String nickName, String role) {
      this.email = email;
      this.password = password;
      this.phone = phone;
      this.nickName = nickName;
      this.role = role;
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  public static class Response {
    private String email;
    private String phone;
    private String nickName;
    private Role role;

    @Builder
    public Response(String email, String phone, String nickName, Role role) {
      this.email = email;
      this.phone = phone;
      this.nickName = nickName;
      this.role = role;
    }
  }
}
