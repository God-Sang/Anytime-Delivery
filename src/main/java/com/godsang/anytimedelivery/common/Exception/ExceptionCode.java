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
  USER_NOT_FOUND(404, "User does not exits"),
  
  // Store
  REGISTRATION_NUMBER_ALREADY_EXISTS(409, "Registration number already exists."),
  TEL_ALREADY_EXISTS(409, "Tel already exists."),
  ADDRESS_ALREADY_EXISTS(409, "Address already exists."),
  NAME_ALREADY_EXISTS(409, "Name already exists."),
  STORE_NOT_FOUND(404, "Store not found."),

  //Address
  ADDRESS_NOT_EXIST(404, "User has no address yet"),

  // Menu
  OWNER_NOT_MATCHED(403, "Owner not matched."),
  MENU_ALREADY_EXISTS(409, "You already have this menu."),
  MENU_NOT_FOUND(404, "Menu not found"),

  // Order
  STORE_CLOSED(409, "Store is closed"),
  NOT_IN_DELIVERY_AREA(409, "Your address is not in the store's delivery area."),
  INVALID_ORDER_STATES_CHANGE(403, "Invalid change of an order state"),
  ORDER_NOT_EXIST(404, "The order does not exist."),
  ORDER_NOT_YOURS(403, "This order is not yours");

  private final int code;
  private final String description;
}
