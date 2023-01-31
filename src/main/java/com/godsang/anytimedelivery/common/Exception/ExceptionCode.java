package com.godsang.anytimedelivery.common.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
  CATEGORY_ALREADY_EXISTS(409, "Category already exists.");
  private final int code;
  private final String description;
}
