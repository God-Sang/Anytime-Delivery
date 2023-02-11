package com.godsang.anytimedelivery.helper;

import com.godsang.anytimedelivery.store.dto.StoreDto;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.user.dto.UserDto;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import lombok.Builder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalTime;
import java.util.List;

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

    public static MultiValueMap<String, String> getMockGetQuery(Integer page, Integer size) {
      MultiValueMap<String, String> queries = new LinkedMultiValueMap<>();
      queries.add("page", String.valueOf(page));
      queries.add("size", String.valueOf(size));
      return queries;
    }
  }


  public static class MockStorePost extends StoreDto.Post {
    @Builder
    private MockStorePost(String registrationNumber, String tel, int deliveryFee, int deliveryTime, String openTime, String closeTime, List<Long> categoryIds, List<String> deliveryAreas) {
      super.setRegistrationNumber(registrationNumber);
      super.setName("애니타임 치킨");
      super.setTel(tel);
      super.setAddress("서울특별시 강남구 강남대로 396 101호");
      super.setDeliveryFee(deliveryFee);
      super.setDeliveryTime(deliveryTime);
      super.setOpenTime(openTime);
      super.setCloseTime(closeTime);
      super.setCategoryIds(categoryIds);
      super.setDeliveryAreas(deliveryAreas);
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
