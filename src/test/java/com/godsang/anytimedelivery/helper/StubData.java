package com.godsang.anytimedelivery.helper;

import com.godsang.anytimedelivery.store.dto.StoreDto;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.user.dto.UserDto;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.persistence.Entity;
import java.time.LocalTime;
import java.util.List;

public class StubData {

  @Entity
  @NoArgsConstructor
  public static class MockStore extends Store {
    @Builder
    private MockStore(long storeId, String registrationNumber, String name, String tel, String address) {
      super.setStoreId(storeId);
      super.setRegistrationNumber(registrationNumber);
      super.setName(name);
      super.setTel(tel);
      super.setAddress(address);
      super.setOpenTime(LocalTime.now());
      super.setCloseTime(LocalTime.now());
      super.setDeliveryFee(6000);
      super.setDeliveryTime(30);
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

  @Entity
  @NoArgsConstructor
  public static class MockUser extends User {
    @Builder
    private MockUser(long userId, String email, String phone, String nickName) {
      super.setUserId(userId);
      super.setEmail(email);
      super.setPassword("1q2w3e4r@");
      super.setPhone(phone);
      super.setNickName(nickName);
      super.setRole(Role.ROLE_CUSTOMER);
    }

    public static User getMockEntity() {
      return MockUser.builder()
          .userId(1L)
          .email("anytime@email.com")
          .phone("010-1234-5678")
          .nickName("애니타임")
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
