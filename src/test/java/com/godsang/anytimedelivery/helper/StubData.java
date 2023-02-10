package com.godsang.anytimedelivery.helper;

import com.godsang.anytimedelivery.address.dto.AddressDto;
import com.godsang.anytimedelivery.address.entity.Address;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.user.dto.UserDto;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalTime;

public class StubData {
  public static class MockStore {
    public static Store getMockEntity(Long storeId, String name) {
      return Store.builder()
          .storeId(storeId)
          .address("서울시 행복동")
          .closeTime(LocalTime.now())
          .deliveryFee(0)
          .openTime(LocalTime.now())
          .deliveryTime(10)
          .introduction("가게")
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
  public static class MockAddress {
    // TODO : deliveryArea 오면 수정 예정
    public static Address getMockAddress(String address, String detailAddress) {
      return Address.builder()
          .address(address)
          .detailAddress(detailAddress)
          .build();
    }
    public static AddressDto.PostRequest getMockAddressPostRequestDto(String address, String detail, String deliveryArea) {
      return AddressDto.PostRequest.builder()
          .address(address)
          .detailAddress(detail)
          .deliveryArea(deliveryArea)
          .build();
    }
  }
}
