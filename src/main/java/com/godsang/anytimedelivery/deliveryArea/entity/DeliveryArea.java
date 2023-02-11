package com.godsang.anytimedelivery.deliveryArea.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DeliveryArea {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long deliveryAreaId;

  @Column(unique = true, nullable = false)
  private String juso;

  public DeliveryArea(String juso) {
    this.juso = juso;
  }
}
