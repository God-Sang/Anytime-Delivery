package com.godsang.anytimedelivery.order.entity;

import com.godsang.anytimedelivery.menu.entity.Option;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderOption {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderOptionId;
  @ManyToOne(optional = false)
  private Option option;
  @ManyToOne(optional = false)
  private OrderGroup orderGroup;

  @Builder
  private OrderOption(Option option, OrderGroup orderGroup) {
    this.option = option;
    this.orderGroup = orderGroup;
  }
}
