package com.godsang.anytimedelivery.store.entity;

import com.godsang.anytimedelivery.category.entity.CategoryStore;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryAreaStore;
import com.godsang.anytimedelivery.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Store {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long storeId;
  @Column(nullable = false, unique = true, length = 12)
  private String registrationNumber;
  @Column(nullable = false, unique = true)
  private String name;
  @Column(nullable = false, unique = true, length = 13)
  private String tel;
  @Column(nullable = false, unique = true)
  private String address;
  private String introduction;
  @Column(nullable = false)
  private LocalTime openTime;
  @Column(nullable = false)
  private LocalTime closeTime;
  @Column(nullable = false)
  private int deliveryFee;
  @Column(nullable = false)
  private int deliveryTime;
  private String mainPhoto1;
  private String mainPhoto2;
  private String mainPhoto3;
  @OneToMany(mappedBy = "store", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
  private List<CategoryStore> categoryStores = new ArrayList<>();
  @OneToMany(mappedBy = "store", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
  private List<DeliveryAreaStore> deliveryAreaStores = new ArrayList<>();
  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  public void addCategoryStore(CategoryStore categoryStore) {
    categoryStores.add(categoryStore);
  }

  public void addDeliveryAreaStore(DeliveryAreaStore deliveryAreaStore) {
    deliveryAreaStores.add(deliveryAreaStore);
  }
}
