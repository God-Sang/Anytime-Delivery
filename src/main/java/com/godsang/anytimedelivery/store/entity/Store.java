package com.godsang.anytimedelivery.store.entity;

import com.godsang.anytimedelivery.category.entity.CategoryStore;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryAreaStore;
import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Store {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long storeId;
  @Column(nullable = false, unique = true, length = 12)
  private String registrationNumber;
  @Column(nullable = false, unique = true)
  private String name;
  @Column(nullable = false, length = 13)
  private String tel;
  @Column(nullable = false)
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
  @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Menu> menus;

  public void addCategoryStore(CategoryStore categoryStore) {
    categoryStores.add(categoryStore);
  }

  public void addDeliveryAreaStore(DeliveryAreaStore deliveryAreaStore) {
    deliveryAreaStores.add(deliveryAreaStore);
  }
  public Store(Long storeId) {
    this.storeId = storeId;
  }

  @Builder
  private Store(Long storeId, String registrationNumber, String name, String tel, String address, LocalTime openTime, LocalTime closeTime, int deliveryFee, int deliveryTime) {
    this.storeId = storeId;
    this.registrationNumber = registrationNumber;
    this.name = name;
    this.tel = tel;
    this.address = address;
    this.openTime = openTime;
    this.closeTime = closeTime;
    this.deliveryFee = deliveryFee;
    this.deliveryTime = deliveryTime;
  }
}
