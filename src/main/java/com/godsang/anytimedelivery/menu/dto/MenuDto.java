package com.godsang.anytimedelivery.menu.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class MenuDto {
  @Getter
  @Setter
  public static class Post extends CommonMenuDto {
    private String description;
    private String photo;
    private List<GroupDto.Post> groups;
  }

  @Getter
  @Setter
  public static class Response extends CommonMenuDto {
    private long menuId;
    private String description;
    private String photo;
    private List<GroupDto.Response> groups;
  }
}
