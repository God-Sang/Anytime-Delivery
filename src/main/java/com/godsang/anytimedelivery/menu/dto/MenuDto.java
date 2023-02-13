package com.godsang.anytimedelivery.menu.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuDto extends CommonMenuDto {
  private String description;
  private String photo;
  private List<GroupDto> groups;
}
