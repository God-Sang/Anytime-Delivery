package com.godsang.anytimedelivery.menu.dto.common;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ParentMenuDto extends MenuDto {
  @NotNull
  private List<GroupDto> group;
}

