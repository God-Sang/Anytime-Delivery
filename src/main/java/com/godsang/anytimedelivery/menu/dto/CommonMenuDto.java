package com.godsang.anytimedelivery.menu.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class CommonMenuDto {
  @NotBlank
  private String name;
  @PositiveOrZero
  private int price;
}
