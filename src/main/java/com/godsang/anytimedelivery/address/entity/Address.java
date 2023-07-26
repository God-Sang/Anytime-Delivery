package com.godsang.anytimedelivery.address.entity;

import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long addressId;
  @Column(nullable = false)
  private String address;
  private String detailAddress;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "delivery_area_id")
  private DeliveryArea deliveryArea;

  public void setDeliveryArea(DeliveryArea deliveryArea) {
    this.deliveryArea = deliveryArea;
  }
}
