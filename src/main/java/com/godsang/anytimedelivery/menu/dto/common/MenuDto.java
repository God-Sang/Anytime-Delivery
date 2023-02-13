package com.godsang.anytimedelivery.menu.dto.common;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class MenuDto {
  @NotBlank
  private String name;
  @PositiveOrZero
  private int price;
  private String description;
}
