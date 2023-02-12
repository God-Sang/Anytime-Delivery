package com.godsang.anytimedelivery.address.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
@Builder
public class AddressDto {
  @NotBlank
  private String deliveryArea; // 시도명 + 시군구명 + 읍면동명, ex) "00시00구00동"
  @NotBlank
  private String address;
  private String detailAddress;
}
