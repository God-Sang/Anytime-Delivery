package com.godsang.anytimedelivery.common.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
  CATEGORY_ALREADY_EXISTS(409, "Category already exists."),
  CATEGORY_NOT_FOUND(404, "No Category found"),
  EMAIL_ALREADY_EXISTS(409, "Email already exists."),
  PHONE_NUMBER_ALREADY_EXISTS(409, "Phone number already exists."),
  NICKNAME_ALREADY_EXISTS(409, "Nickname already exists."),
  REGISTRATION_NUMBER_ALREADY_EXISTS(409, "Registration number already exists."),
  TEL_ALREADY_EXISTS(409, "Tel already exists."),
  ADDRESS_ALREADY_EXISTS(409, "Address already exists.");

  private final int code;
  private final String description;
}
