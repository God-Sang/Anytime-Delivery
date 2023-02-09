package com.godsang.anytimedelivery.category.dto;

import lombok.Getter;
import lombok.Setter;

public class CategoryDto {
  @Getter
  @Setter
  public static class Post {
    private String name;
  }

  @Getter
  @Setter
  public static class Patch {
    private String preName;
    private String curName;
  }
}
