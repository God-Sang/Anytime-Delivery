package com.godsang.anytimedelivery.store.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
public class Store {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long storeId;
  @Column(nullable = false)
  private Long registrationNumber;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String tel;
  @Column(nullable = false)
  private String address;
  private String info;
  @Column(nullable = false)
  private LocalTime open_time;
  @Column(nullable = false)
  private LocalTime close_time;
  @Column(nullable = false)
  private int delivery_fee;
}
