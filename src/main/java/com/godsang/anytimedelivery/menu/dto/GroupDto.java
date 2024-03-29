package com.godsang.anytimedelivery.menu.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

public class GroupDto {
  @Getter
  @Setter
  public static class Post {
    @NotBlank
    private String title;
    @Pattern(regexp = "^(CHECK|RADIO)$", message = "CHECK 또는 RADIO 중 하나를 입력해야 합니다.")
    private String choiceType;
    private List<OptionDto.Post> options;
  }

  @Getter
  @Setter
  public static class Response {
    private long groupId;
    private String title;
    private String choiceType;
    private List<OptionDto.Response> options;
  }
}
