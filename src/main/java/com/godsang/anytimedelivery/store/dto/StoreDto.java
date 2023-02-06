package com.godsang.anytimedelivery.store.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;


public class StoreDto {
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
