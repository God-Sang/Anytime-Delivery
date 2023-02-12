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
    public static MultiValueMap<String, String> getMockGetQuery(Integer page, Integer size) {
      MultiValueMap<String, String> queries = new LinkedMultiValueMap<>();
      queries.add("page", String.valueOf(page));
      queries.add("size", String.valueOf(size));
      return queries;
    }

    public static Store getMockEntity(Long storeId, String registrationNumber, String name, String tel, String address) {
      return Store.builder()
          .storeId(storeId)
          .registrationNumber(registrationNumber)
          .name(name)
          .tel(tel)
          .address(address)
          .openTime(LocalTime.now())
          .closeTime(LocalTime.now())
          .deliveryFee(6000)
          .deliveryTime(30)
          .build();
    }
  }


  public static class MockStorePost extends StoreDto.Post {
    @Builder
    private MockStorePost(String registrationNumber, String name, String address, String tel, int deliveryFee, int deliveryTime, String openTime, String closeTime, List<Long> categoryIds, List<String> deliveryAreas) {
      super.setRegistrationNumber(registrationNumber);
      super.setName(name);
      super.setTel(tel);
      super.setAddress(address);
      super.setDeliveryFee(deliveryFee);
      super.setDeliveryTime(deliveryTime);
      super.setOpenTime(openTime);
      super.setCloseTime(closeTime);
      super.setCategoryIds(categoryIds);
      super.setDeliveryAreas(deliveryAreas);
    }
  }

  public static class MockUser {

    public static User getMockEntity(long userId, String email, String phone, String nickName) {
      return User.builder()
          .userId(userId)
          .email(email)
          .password("1q2w3e4r@")
          .phone(phone)
          .nickName(nickName)
          .role(Role.ROLE_CUSTOMER)
          .build();
    }

    public static User getMockEntity() {
      return User.builder()
          .userId(1L)
          .email("anytime@email.com")
          .phone("010-1234-5678")
          .nickName("애니타임")
          .password("1q2w3e4r@")
          .role(Role.ROLE_CUSTOMER)
          .build();
    }
  }

  public static class MockUserPost extends UserDto.Post {
    @Builder
    private MockUserPost(String email, String password, String phone, String nickName) {
      super.setEmail(email);
      super.setPassword(password);
      super.setPhone(phone);
      super.setNickName(nickName);
      super.setRole("customer");
    }
  }
}
