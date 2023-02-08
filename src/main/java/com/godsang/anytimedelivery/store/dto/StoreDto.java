package com.godsang.anytimedelivery.store.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;


public class StoreDto {
  @Getter
  @Setter
  public static class Response {
    private Long storeId;
    private String registrationNumber;
    private String name;
    private String tel;
    private String address;
    private String introduction;
    private LocalTime openTime;
    private LocalTime closeTime;
    private int deliveryFee;
    private int deliveryTime;
    private String mainPhoto1;
    private String mainPhoto2;
    private String mainPhoto3;
  }
}
