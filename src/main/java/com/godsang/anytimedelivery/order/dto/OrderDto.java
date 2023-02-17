package com.godsang.anytimedelivery.order.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

public class OrderDto {
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
