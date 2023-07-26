package com.godsang.anytimedelivery.helper.stub;

import com.godsang.anytimedelivery.address.dto.AddressDto;
import com.godsang.anytimedelivery.menu.dto.GroupDto;
import com.godsang.anytimedelivery.menu.dto.MenuDto;
import com.godsang.anytimedelivery.menu.dto.OptionDto;
import com.godsang.anytimedelivery.order.dto.OrderDto;
import com.godsang.anytimedelivery.order.entity.OrderStatus;
import com.godsang.anytimedelivery.store.dto.StoreDto;
import com.godsang.anytimedelivery.user.dto.UserDto;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
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
          OrderDto.OrderGroupDto groupDto = new OrderDto.OrderGroupDto();
          ReflectionTestUtils.setField(groupDto, "groupId", 1L);
          ReflectionTestUtils.setField(groupDto, "optionIds", optionIds);
          groups.add(groupDto);
        }
        OrderDto.OrderMenuDto menuDto = new OrderDto.OrderMenuDto();
        ReflectionTestUtils.setField(menuDto, "menuId", 1L);
        ReflectionTestUtils.setField(menuDto, "amount", 3);
        ReflectionTestUtils.setField(menuDto, "groups", groups);
        menus.add(menuDto);
      }
      OrderDto.Post post = new OrderDto.Post();
      ReflectionTestUtils.setField(post, "request", "많이 주세요");
      ReflectionTestUtils.setField(post, "foodTotalPrice", 20000);
      ReflectionTestUtils.setField(post, "storeId", 1L);
      ReflectionTestUtils.setField(post, "menus", menus);
      return post;
    }
  }

  public static class OrderResponse {
    public static OrderDto.Customer getCustomer() {
      return OrderDto.Customer.builder()
          .address("서울시 강남구 서초동")
          .detailAddress("204호")
          .phone("010-1111-1111")
          .nickName("서초동 햄주먹")
          .build();
    }

    public static OrderDto.OptionResponse getOption() {
      return OrderDto.OptionResponse.builder()
          .name("콜라")
          .price(2000)
          .build();
    }
    public static OrderDto.GroupResponse getGroup() {
      return OrderDto.GroupResponse.builder()
          .options(List.of(getOption(), getOption()))
          .title("추가 메뉴")
          .build();
    }
    public static OrderDto.MenuResponse getMenu() {
      return OrderDto.MenuResponse.builder()
          .name("짜장면")
          .price(2000)
          .groups(List.of(getGroup(), getGroup()))
          .amount(1)
          .build();
    }
    public static OrderDto.Response get(OrderStatus orderStatus) {
      return OrderDto.Response.builder()
          .orderStatus(orderStatus.name())
          .customer(getCustomer())
          .orderId(1L)
          .foodTotalPrice(10000)
          .menus(List.of(getMenu(), getMenu()))
          .orderTime(LocalDateTime.now())
          .deliveryFee(10000)
          .build();
    }
    public static OrderDto.ResponseForList getForList(Long orderId) {
      return OrderDto.ResponseForList.builder()
          .orderId(orderId)
          .orderTime(LocalDateTime.now())
          .detailAddress("서울시 행복구 행복동")
          .detailAddress("205호")
          .deliveryTime((short) 30)
          .request("조심히 와주세요")
          .foodTotalPrice(10000)
          .build();
    }

    public static List<OrderDto.ResponseForList> getList() {
      List<OrderDto.ResponseForList> responses = new ArrayList<>();
      for (long orderId = 1; orderId <= 5; orderId++) {
        responses.add(getForList(orderId));
      }
      return responses;
    }
  }

  public static class OrderPatch {
    public static OrderDto.Patch get(OrderStatus orderStatus) {
      OrderDto.Patch patch = new OrderDto.Patch();
      ReflectionTestUtils.setField(patch, "orderStatus", orderStatus);
      return patch;
    }
  }

  public static class OrderPatchCancel {
    public static OrderDto.PatchCancel get(String reason) {
      OrderDto.PatchCancel patchCancel = new OrderDto.PatchCancel();
      ReflectionTestUtils.setField(patchCancel, "reason", reason);
      return patchCancel;
    }
  }
}
