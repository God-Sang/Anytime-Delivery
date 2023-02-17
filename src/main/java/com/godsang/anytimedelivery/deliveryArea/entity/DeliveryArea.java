package com.godsang.anytimedelivery.deliveryArea.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

  public DeliveryArea(Long deliveryAreaId, String juso) {
    this.deliveryAreaId = deliveryAreaId;
    this.juso = juso;
  }
}
