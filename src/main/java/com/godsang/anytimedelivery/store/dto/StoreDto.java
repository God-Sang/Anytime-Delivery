package com.godsang.anytimedelivery.store.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
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
    @Size(min = 1, max = 3, message = "최소 한 개, 최대 세 개까지 선택 가능합니다.")
    @UniqueElements(message = "중복된 값이 존재합니다.")
    @NotNull
    private List<@Positive Long> categoryIds;
    @Size(min = 1, message = "한 개 이상의 배달 가능 지역을 선택해야 합니다.")
    @UniqueElements(message = "중복된 값이 존재합니다.")
    @NotNull
    private List<@Pattern(regexp = "^[가-힣]{2,}(시|도)\\s[가-힣]+(시|군|구)\\s[가-힣]+(읍|면|동)$") String> deliveryAreas;
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
