package com.godsang.anytimedelivery.menu.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Option {
  private String name;
  private int price;

  @Builder
  private Option(String name, int price) {
    this.name = name;
    this.price = price;
  }
}
