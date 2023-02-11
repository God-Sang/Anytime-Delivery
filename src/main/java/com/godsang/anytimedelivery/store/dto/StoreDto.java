package com.godsang.anytimedelivery.store.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalTime;
import java.util.List;


public class StoreDto {
  @Getter
  @Setter
  public static class Post {
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}$", message = "올바르지 않은 사업자 등록번호입니다.")
    @NotBlank
    private String registrationNumber;
    @Pattern(regexp = "^[가-힣a-zA-Z\\d\\s]+$", message = "한글, 영어, 숫자만 입력 가능합니다.")
    @NotBlank
    private String name;
    @Pattern(regexp = "^0\\d{1,2}-\\d{3,4}-\\d{4}$", message = "올바르지 않은 전화번호입니다.")
    @NotBlank
    private String tel;
    @NotBlank
    private String address;
    private String introduction;
    @Pattern(regexp = "^(0\\d|1\\d|2[0-3]):[0-5][0-9]$", message = "올바르지 않은 오픈 시간입니다.")
    @NotBlank
    private String openTime;
    @Pattern(regexp = "^(0\\d|1\\d|2[0-3]):[0-5][0-9]$", message = "올바르지 않은 마감 시간입니다.")
    @NotBlank
    private String closeTime;
    @PositiveOrZero(message = "0 또는 양수만 입력할 수 있습니다.")
    @Max(value = 10000, message = "최대 10,000원까지 설정할 수 있습니다.")
    @NotNull
    private Integer deliveryFee;
    @PositiveOrZero(message = "0 또는 양수만 입력할 수 있습니다.")
    @Max(value = 100, message = "최대 100분까지 설정할 수 있습니다.")
    @NotNull
    private Integer deliveryTime;
    @NotNull
    private List<@Positive Long> categoryIds; // TODO validation 중복 요소, 0보다 큰지, 요소가 비었는지(blank)
    @NotNull
    private List<String> deliveryAreas; // TODO validation 중복 요소, ~시/도 ~시/군/구 ~읍/면/동, 요소가 비었는지(blank)
    private String mainPhoto1;
    private String mainPhoto2;
    private String mainPhoto3;

  }

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
