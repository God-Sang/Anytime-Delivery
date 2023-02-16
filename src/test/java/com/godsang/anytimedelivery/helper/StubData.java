package com.godsang.anytimedelivery.helper;


import com.godsang.anytimedelivery.address.dto.AddressDto;
import com.godsang.anytimedelivery.address.entity.Address;
import com.godsang.anytimedelivery.menu.dto.GroupDto;
import com.godsang.anytimedelivery.menu.dto.MenuDto;
import com.godsang.anytimedelivery.menu.dto.OptionDto;
import com.godsang.anytimedelivery.menu.entity.ChoiceType;
import com.godsang.anytimedelivery.menu.entity.Group;
import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.menu.entity.Option;
import com.godsang.anytimedelivery.store.dto.StoreDto;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.user.dto.UserDto;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import lombok.Builder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class StubData {
  public static class Query {
    public static MultiValueMap<String, String> getPageQuery(Integer page, Integer size) {
      MultiValueMap<String, String> queries = new LinkedMultiValueMap<>();
      queries.add("page", String.valueOf(page));
      queries.add("size", String.valueOf(size));
      return queries;
    }
  }

  public static class MockStore {
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

    public static Store getMockEntity() {
      return Store.builder()
          .storeId(1L)
          .registrationNumber("123-12-12345")
          .name("애니타임 치킨")
          .tel("02-123-1234")
          .address("경기도 성남시 분당구 123")
          .openTime(LocalTime.of(9, 30))
          .closeTime(LocalTime.of(21, 30))
          .deliveryFee(1000)
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

    public static User getMockEntity(Role role) {
      return User.builder()
          .userId(1L)
          .email("anytime@email.com")
          .phone("010-1234-5678")
          .nickName("애니타임")
          .password("1q2w3e4r@")
          .role(role)
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

  public static class MockAddress {
    // TODO : deliveryArea 오면 수정 예정
    public static Address getMockAddress(String address, String detailAddress) {
      return Address.builder()
          .address(address)
          .detailAddress(detailAddress)
          .build();
    }
  }

  public static class MockAddressDto extends AddressDto {
    @Builder
    private MockAddressDto(String address, String detailAddress, String deliveryArea) {
      super.setAddress(address);
      super.setDetailAddress(detailAddress);
      super.setDeliveryArea(deliveryArea);
    }

    public static AddressDto getMockAddressPostRequestDto(String address, String detail, String deliveryArea) {
      return MockAddressDto.builder()
          .address(address)
          .detailAddress(detail)
          .deliveryArea(deliveryArea)
          .build();
    }
  }

  public static class MockMenu {
    public static Menu getMockMenuEntity(String name, int price) {
      return Menu.builder()
          .name(name)
          .price(price)
          .description("메뉴 설명")
          .photo("사진 경로")
          .build();
    }

    public static Group getMockGroupEntity(String title, ChoiceType choiceType) {
      return Group.builder()
          .title(title)
          .choiceType(choiceType)
          .build();
    }

    public static Option getOption(String name, int price, Group group) {
      return Option.builder()
          .name(name)
          .price(price)
          .group(group)
          .build();
    }

    public static List<Option> getOptionList(Group group) {
      List<Option> options = new ArrayList<>();
      for (int i = 0; i < 10; i++) {
        Option option = getOption("매운맛" + i, 1000, group);
        options.add(option);
      }
      return options;
    }

    public static List<Group> getGroupList(Menu menu) {
      List<Group> groups = new ArrayList<>();
      for (int i = 0; i < 5; i++) {
        Group group = getMockGroupEntity("맛 선택", ChoiceType.RADIO);
        List<Option> options = getOptionList(group);
        group.setOptions(options);
        group.setMenu(menu);
        groups.add(group);
      }
      return groups;
    }

    public static Menu getMockMenuEntity() {
      Menu menu = getMockMenuEntity("떡볶이", 10000);
      List<Group> groups = getGroupList(menu);
      menu.setGroups(groups);
      return menu;
    }
  }


  public static class MockMenuPost extends MenuDto.Post {
    @Builder
    private MockMenuPost(String name, int price, List<GroupDto.Post> groups) {
      super.setName(name);
      super.setPrice(price);
      super.setGroups(groups);
    }
    public static MenuDto.Post getMenuDto(String name, int price, List<GroupDto.Post> groups) {
      MenuDto.Post post = new MenuDto.Post();
      post.setName(name);
      post.setPrice(price);
      post.setDescription("설명입니다.");
      post.setPhoto("사진");
      post.setGroups(groups);
      return post;
    }
    public static MenuDto.Post getMenuDto() {
      MenuDto.Post post = new MenuDto.Post();
      post.setName("떡볶이");
      post.setPrice(10000);
      post.setDescription("설명입니다.");
      post.setPhoto("사진");
      post.setGroups(getGroupDtoList());
      return post;
    }

    public static GroupDto.Post getGroupDto(String title, String choiceType, List<OptionDto.Post> optionDtos) {
      GroupDto.Post groupDto = new GroupDto.Post();
      groupDto.setTitle(title);
      groupDto.setChoiceType(choiceType);
      groupDto.setOptions(optionDtos);
      return groupDto;
    }

    public static OptionDto.Post getOptionDto(String name, int price) {
      OptionDto.Post optionPostDto = new OptionDto.Post();
      optionPostDto.setName(name);
      optionPostDto.setPrice(price);
      return optionPostDto;
    }

    public static List<OptionDto.Post> getOptionDtoList() {
      List<OptionDto.Post> posts = new ArrayList<>();
      for (int i = 0; i < 10; i++) {
        posts.add(StubData.MockMenuPost.getOptionDto("착한맛" + i, 1000));
      }
      return posts;
    }

    public static List<GroupDto.Post> getGroupDtoList() {
      List<GroupDto.Post> posts = new ArrayList<>();
      for (int i = 0; i < 5; i++) {
        List<OptionDto.Post> optionPostDtos = StubData.MockMenuPost.getOptionDtoList();
        GroupDto.Post post = StubData.MockMenuPost.getGroupDto("맛 선택" + i, "RADIO", optionPostDtos);
        posts.add(post);
      }
      return posts;
    }
  }
}
