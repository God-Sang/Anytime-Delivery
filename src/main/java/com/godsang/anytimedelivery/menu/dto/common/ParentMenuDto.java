package com.godsang.anytimedelivery.menu.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ParentMenuDto extends MenuDto {
  private String photo;
  private List<GroupDto> group;
}

