package com.godsang.anytimedelivery.order.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {
  @Getter
  @Builder
  public static class ResponseForList {
    private Long orderId;
    private String address;
    private String detailAddress;
    private String request;
    private Short deliveryTime;
    private LocalDateTime orderTime;
    private int foodTotalPrice;

  }

  @Getter
  @Builder
  public static class Response {
    private Long orderId;
    private String orderStatus;
    private int foodTotalPrice;
    private Integer deliveryFee;
    private Short deliveryTime;
    private Customer customer;
    private LocalDateTime orderTime;
    private List<MenuResponse> menus;
  }

  @Getter
  @Builder
  public static class Customer {
    private String nickName;
    private String phone;
    private String address;
    private String detailAddress;
  }

  @Getter
  @Builder
  public static class MenuResponse {
    private Integer amount;
    private String name;
    private int price;
    private List<GroupResponse> groups;
  }

  @Getter
  @Builder
  public static class GroupResponse {
    private String title;
    private List<OptionResponse> optionResponses;
  }

  @Getter
  @Builder
  public static class OptionResponse {
    private String name;
    private int price;
  }

  @Getter
  @Builder
  public static class Post {
    private String request;
    @NotBlank
    @Positive
    private Long storeId;
    private List<OrderMenuDto> menus;
  }

  @Getter
  @Builder
  public static class OrderMenuDto {
    @NotBlank
    @Positive
    private Long menuId;
    @NotBlank
    @Positive
    private Integer amount;
    private List<OrderGroupDto> groups;
  }

  @Getter
  @Builder
  public static class OrderGroupDto {
    @NotBlank
    @Positive
    private Long groupId;
    private List<Long> optionIds;
  }
}
