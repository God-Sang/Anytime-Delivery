package com.godsang.anytimedelivery.menu.dto;

import lombok.Getter;
import lombok.Setter;

public class OptionDto {
  @Getter
  @Setter
  public static class Post extends CommonMenuDto{

  }

  @Getter
  @Setter
  public static class Response extends CommonMenuDto{
    Long optionId;
  }

}
