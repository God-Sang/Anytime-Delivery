package com.godsang.anytimedelivery.store.entity;

import com.godsang.anytimedelivery.category.entity.CategoryStore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Store {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long storeId;
  @Column(nullable = false)
  private String registrationNumber;
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
  @Column(nullable = false)
  private int delivery_time;
  @Column
  private String mainPhoto1;
  @Column
  private String mainPhoto2;
  @Column
  private String mainPhoto3;

  @OneToMany(mappedBy = "store", cascade = CascadeType.PERSIST)
  private List<CategoryStore> categoryStores = new ArrayList<>();

  public void addCategoryStore(CategoryStore categoryStore) {
    categoryStores.add(categoryStore);
  }
}
