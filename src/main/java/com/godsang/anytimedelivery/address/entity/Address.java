package com.godsang.anytimedelivery.address.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
  // TODO: deliveryArea와 매핑하기
}
