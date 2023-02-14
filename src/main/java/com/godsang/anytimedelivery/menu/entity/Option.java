package com.godsang.anytimedelivery.menu.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "OPTIONZ") // option, options는 에약어
public class Option {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long optionsId;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private int price;
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "group_id")
  private Group group;

  @Builder
  private Option(String name, int price, Group group) {
    this.name = name;
    this.price = price;
    this.group = group;
  }
}
