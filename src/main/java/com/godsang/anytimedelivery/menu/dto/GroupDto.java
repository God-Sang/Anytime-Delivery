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
    @Pattern(regexp = "^(check|radio)$", message = "check 또는 radio 중 하나를 입력해야 합니다.")
    private String choiceType;
    private List<OptionDto> options;
  }

  @Getter
  @Setter
  public static class Response {
    private long groupId;
    private String title;
    private String choiceType;
    private List<OptionDto> options;
  }
}
