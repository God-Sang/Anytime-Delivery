package com.godsang.anytimedelivery.deliveryArea.entity;

import com.godsang.anytimedelivery.store.entity.Store;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DeliveryAreaStore {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long deliveryAreaStoreId;

  @ManyToOne(fetch = FetchType.LAZY)
  private Store store;

  @ManyToOne(fetch = FetchType.LAZY)
  private DeliveryArea deliveryArea;

  public DeliveryAreaStore(Store store, DeliveryArea deliveryArea) {
    this.store = store;
    this.deliveryArea = deliveryArea;
  }
}
