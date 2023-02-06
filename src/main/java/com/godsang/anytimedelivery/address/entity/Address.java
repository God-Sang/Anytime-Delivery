package com.godsang.anytimedelivery.address.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@RedisHash("address")
public class Address {
  @Id
  private Long userId;
  private String oldAddress;
  private String newAddress;
  private String detailAddress;
  private String dong;
}
