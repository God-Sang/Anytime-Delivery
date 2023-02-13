package com.godsang.anytimedelivery.menu.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Option {
  @Id
  private Long optionId;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private int price;

  @Builder
  private Option(String name, int price) {
    this.name = name;
    this.price = price;
  }
}
