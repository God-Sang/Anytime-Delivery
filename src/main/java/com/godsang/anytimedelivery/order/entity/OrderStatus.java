package com.godsang.anytimedelivery.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
  WAIT, ACCEPTED, DELIVERED, CANCELED;
}
