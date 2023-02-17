package com.godsang.anytimedelivery.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
  IN_CART("inCart"), WAIT("wait"), ACCEPTED("accepted"), DELIVERED("delivered"), CANCELED("canceled");
  private final String name;

  OrderStatus(String name) {
    this.name = name;
  }
}