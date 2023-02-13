package com.godsang.anytimedelivery.menu.dto;

import com.godsang.anytimedelivery.menu.dto.common.ParentMenuDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class StoreMenuDto {
  @NotNull
  private List<ParentMenuDto> menu;
}
