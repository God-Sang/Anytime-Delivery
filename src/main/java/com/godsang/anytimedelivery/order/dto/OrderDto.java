package com.godsang.anytimedelivery.order.dto;

import com.godsang.anytimedelivery.common.validator.EnumNamePattern;
import com.godsang.anytimedelivery.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ResponseForList {
    private Long orderId;
    private String storeName;
    private String address;
    private String detailAddress;
    private String request;
    private int deliveryTime;
    private int deliveryFee;
    private LocalDateTime orderTime;
    private int foodTotalPrice;

  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {
    private Long orderId;
    private String storeName;
    private String orderStatus;
    private int foodTotalPrice;
    private int deliveryFee;
    private int deliveryTime;
    private Customer customer;
    private LocalDateTime orderTime;
    private List<MenuResponse> menus;
  }

  @Getter
  public static class Post {
    private String request;
    @Positive
    private Long storeId;
    @Positive
    private int foodTotalPrice;
    private List<OrderMenuDto> menus;
  }

  @Getter
  public static class Patch {
    @EnumNamePattern(regexp = "ACCEPTED|DELIVERED")
    private OrderStatus orderStatus;
  }

  @Getter
  public static class PatchCancel {
    @NotBlank
    private String reason;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Customer {
    private String nickName;
    private String phone;
    private String address;
    private String detailAddress;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class MenuResponse {
    private int amount;
    private String name;
    private int price;
    private List<GroupResponse> groups;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GroupResponse {
    private String title;
    private List<OptionResponse> options;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OptionResponse {
    private String name;
    private int price;
  }

  @Getter
  public static class OrderMenuDto {
    @Positive
    private Long menuId;
    @Positive
    private Integer amount;
    private List<OrderGroupDto> groups;
  }

  @Getter
  public static class OrderGroupDto {
    @Positive
    private Long groupId;
    private List<Long> optionIds;
  }
}
