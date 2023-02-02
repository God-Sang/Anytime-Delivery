package com.godsang.anytimedelivery.helper;

import com.godsang.anytimedelivery.user.dto.UserDto;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;

public class StubData {
  public static class MockUser {
    public static User getMockEntity(long userId, String email, String password, String phone, String nickName, Role role) {
      return User.builder()
          .userId(userId)
          .email(email)
          .password(password)
          .phone(phone)
          .nickName(nickName)
          .role(role)
          .build();
    }
  }

  public static class MockUserDto {
    public static UserDto.Post getMockPost(String email, String password, String phone, String nickName, String role) {
      return UserDto.Post.builder()
          .email(email)
          .password(password)
          .phone(phone)
          .nickName(nickName)
          .role(role)
          .build();
    }

    public static UserDto.Response getMockResponse(String email, String phone, String nickName, Role role) {
      return UserDto.Response.builder()
          .email(email)
          .phone(phone)
          .nickName(nickName)
          .role(role)
          .build();
    }
  }
}
