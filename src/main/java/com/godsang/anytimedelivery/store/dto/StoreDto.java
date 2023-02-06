package com.godsang.anytimedelivery.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalTime;


public class StoreDto {
  @Getter
  @Setter
  @AllArgsConstructor
  public static class GetRequest {
    @Positive
    @NotNull
    private Long categoryId;
    @PositiveOrZero
    @NotNull
    private Integer page;
    @PositiveOrZero
    @NotNull
    private Integer size;
  }

  @Getter
  @Setter
  public static class GetResponse {
    private Long storeId;
    private String registrationNumber;
    private String name;
    private String tel;
    private String address;
    private String info;
    private LocalTime open_time;
    private LocalTime close_time;
    private int delivery_fee;
    private int delivery_time;
    private String mainPhoto1;
    private String mainPhoto2;
    private String mainPhoto3;
  }
}
