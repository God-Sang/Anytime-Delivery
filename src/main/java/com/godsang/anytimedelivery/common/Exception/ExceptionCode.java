package com.godsang.anytimedelivery.common.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
  // Category
  CATEGORY_ALREADY_EXISTS(409, "Category already exists."),
  CATEGORY_NOT_FOUND(404, "No Category found"),

  // User
  EMAIL_ALREADY_EXISTS(409, "Email already exists."),
  PHONE_NUMBER_ALREADY_EXISTS(409, "Phone number already exists."),
  NICKNAME_ALREADY_EXISTS(409, "Nickname already exists."),
  USER_NOT_FOUND(404, "User does not exits");

  private final int code;
  private final String description;
}
