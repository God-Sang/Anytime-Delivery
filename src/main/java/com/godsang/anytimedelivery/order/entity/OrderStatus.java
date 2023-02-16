package com.godsang.anytimedelivery.order.entity;

public enum OrderStatus {
  IN_CART("inCart"), WAIT("wait"), ACCEPTED("accepted"), DELIVERED("delivered"), CANCELED("canceled");
  private String name;

  OrderStatus(String name) {
    this.name = name;
  }
}
