package com.godsang.anytimedelivery.category.entity;

import com.godsang.anytimedelivery.store.entity.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class CategoryStore {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long categoryStoreId;
  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;
  @ManyToOne(fetch = FetchType.LAZY)
  private Store store;

  public CategoryStore(Category category, Store store) {
    this.category = category;
    this.store = store;
  }
}
