package com.godsang.anytimedelivery.helper.stub;

import com.godsang.anytimedelivery.address.dto.AddressDto;
import com.godsang.anytimedelivery.menu.dto.GroupDto;
import com.godsang.anytimedelivery.menu.dto.MenuDto;
import com.godsang.anytimedelivery.menu.dto.OptionDto;
import com.godsang.anytimedelivery.order.dto.OrderDto;
import com.godsang.anytimedelivery.store.dto.StoreDto;
import com.godsang.anytimedelivery.user.dto.UserDto;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MockDto {
  public static class Query {
    public static MultiValueMap<String, String> getPageQuery(Integer page, Integer size) {
      MultiValueMap<String, String> queries = new LinkedMultiValueMap<>();
      queries.add("page", String.valueOf(page));
      queries.add("size", String.valueOf(size));
      return queries;
    }
  }

  public static class UserPost {
    public static UserDto.Post get(String email, String password, String phone, String nickName) {
      UserDto.Post userPost = new UserDto.Post();
      userPost.setEmail(email);
      userPost.setPassword(password);
      userPost.setPhone(phone);
      userPost.setNickName(nickName);
      userPost.setRole("customer");
      return userPost;
    }

    public static UserDto.Post get() {
      return get("anytime@email.com", "1q2w3e4r@", "010-1234-5678", "애니타임");
    }
  }

  public static class StorePost {
    public static StoreDto.Post get(String registrationNumber, String name, String tel, String address, int deliveryFee, int deliveryTime, String openTime, String closeTime, List<Long> categoryIds, List<String> deliveryAreas) {
      StoreDto.Post storePost = new StoreDto.Post();
      storePost.setRegistrationNumber(registrationNumber);
      storePost.setName(name);
      storePost.setTel(tel);
      storePost.setIntroduction("가게 설명");
      storePost.setAddress(address);
      storePost.setDeliveryFee(deliveryFee);
      storePost.setDeliveryTime(deliveryTime);
      storePost.setOpenTime(openTime);
      storePost.setCloseTime(closeTime);
      storePost.setCategoryIds(categoryIds);
      storePost.setDeliveryAreas(deliveryAreas);
      return storePost;
    }

    public static StoreDto.Post get() {
      return get("123-12-12345",
          "애니타임 치킨",
          "02-123-4567",
          "서울특별시 강남구 강남대로 123길 101호",
          3000,
          60,
          "00:00",
          "23:59",
          List.of(1L),
          List.of("서울특별시 강남구 역삼동", "서울특별시 강남구 삼성동")
      );
    }
  }

  public static class AddressPost {
    public static AddressDto get(String address, String detailAddress, String deliveryArea) {
      AddressDto addressPost = new AddressDto();
      addressPost.setAddress(address);
      addressPost.setDetailAddress(detailAddress);
      addressPost.setDeliveryArea(deliveryArea);
      return addressPost;
    }

    public static AddressDto get() {
      return get("서울특별시 강남구 역삼동", "123길 101호", "서울특별시 강남구 역삼동");
    }
  }

  public static class MenuPost {
    public static MenuDto.Post getOption(String menuName, int menuPrice, String choiceType) {
      MenuDto.Post menuPost = new MenuDto.Post();
      menuPost.setName(menuName);
      menuPost.setPrice(menuPrice);
      menuPost.setDescription("메뉴 설명");
      menuPost.setPhoto("메뉴 사진");
      menuPost.setGroups(getGroupPost(choiceType));
      return menuPost;
    }

    public static MenuDto.Post getOption() {
      return getOption("떡볶이", 10000, "CHECK");
    }

    public static List<GroupDto.Post> getGroupPost(String choiceType) {
      List<String> titles = List.of("맛 선택", "소스 추가", "치즈볼 추가", "음료 선택");
      List<List<OptionDto.Post>> options = getOptionList();

      List<GroupDto.Post> groupPosts = new ArrayList<>();
      for (int i = 0; i < titles.size(); i++) {
        GroupDto.Post groupPost = getGroup(titles.get(i), choiceType, options.get(i));
        groupPosts.add(groupPost);
      }
      return groupPosts;
    }

    public static GroupDto.Post getGroup(String title, String choiceType, List<OptionDto.Post> options) {
      GroupDto.Post groupPost = new GroupDto.Post();
      groupPost.setTitle(title);
      groupPost.setChoiceType(choiceType);
      groupPost.setOptions(options);
      return groupPost;
    }

    private static List<List<OptionDto.Post>> getOptionList() {
      List<List<OptionDto.Post>> optionPost = new ArrayList<>();
      optionPost.add(getOptions(Map.of("간장맛", 0, "마늘맛", 0, "민트맛", 0)));
      optionPost.add(getOptions(Map.of("고추마요소스", 1000, "초코소스", 1000, "볼케이노소스", 1000)));
      optionPost.add(getOptions(Map.of("와사비 치즈볼", 5000, "불닭 치즈볼", 5000, "크림 치즈볼", 5000)));
      optionPost.add(getOptions(Map.of("콜라", 2000, "사이다", 2000, "맥주", 4000)));

      return optionPost;
    }

    private static List<OptionDto.Post> getOptions(Map<String, Integer> options) {
      List<OptionDto.Post> optionPosts = new ArrayList<>();

      for (Map.Entry<String, Integer> option : options.entrySet()) {
        OptionDto.Post optionPost = getOption(option.getKey(), option.getValue());
        optionPosts.add(optionPost);
      }
      return optionPosts;
    }

    public static List<OptionDto.Post> getOptions() {
      return getOptions(Map.of("옵션-1", 0, "옵션-2", 0, "옵션-3", 0));
    }

    public static OptionDto.Post getOption(String name, int price) {
      OptionDto.Post postDto = new OptionDto.Post();
      postDto.setName(name);
      postDto.setPrice(price);
      return postDto;
    }
  }

  public static class OrderPost {
    public static OrderDto.Post get() {
      List<OrderDto.OrderMenuDto> menus = new ArrayList<>();
      for (int m = 0; m < 3; m++) {
        List<OrderDto.OrderGroupDto> groups = new ArrayList<>();
        for (int i = m; i < m + 3; i++) {
          List<Long> optionIds = List.of(1L, 2L, 3L, 4L);
          OrderDto.OrderGroupDto groupDto = OrderDto.OrderGroupDto.builder()
              .groupId(1L)
              .optionIds(optionIds)
              .build();
          groups.add(groupDto);
        }
        OrderDto.OrderMenuDto menuDto = OrderDto.OrderMenuDto.builder()
            .menuId(1L)
            .amount(3)
            .groups(groups)
            .build();
        menus.add(menuDto);
      }
      OrderDto.Post post = OrderDto.Post.builder()
          .request("많이 주세요")
          .storeId(1L)
          .menus(menus)
          .build();
      return post;
    }
  }
}
