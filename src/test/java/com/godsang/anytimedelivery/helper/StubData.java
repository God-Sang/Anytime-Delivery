package com.godsang.anytimedelivery.helper;

import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.user.dto.UserDto;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalTime;

public class StubData {
  public static class MockStore {
    public static Store getMockEntity(Long storeId, String name) {
      return Store.builder()
          .storeId(storeId)
          .address("서울시 행복동")
          .close_time(LocalTime.now())
          .delivery_fee(0)
          .open_time(LocalTime.now())
          .delivery_time(10)
          .info("가게")
          .name(name)
          .registrationNumber("1-2-3")
          .mainPhoto1("mainPhoto1")
          .mainPhoto2("mainPhoto1")
          .mainPhoto3("mainPhoto1")
          .build();
    }

    public static MultiValueMap<String, String> getMockGetQuery(Long categoryId, Integer page, Integer size) {
      MultiValueMap<String, String> queries = new LinkedMultiValueMap<>();
      queries.add("category-id", String.valueOf(categoryId));
      queries.add("page", String.valueOf(page));
      queries.add("size", String.valueOf(size));
      return queries;
    }
  }

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
