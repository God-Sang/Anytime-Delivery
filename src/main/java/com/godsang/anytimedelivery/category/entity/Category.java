package com.godsang.anytimedelivery.category.entity;

import com.godsang.anytimedelivery.store.entity.Store;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Categories for stores. Only an admin can manage it.
 */

@Entity
@Getter
@NoArgsConstructor
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long categoryId;
  @Column(unique = true, nullable = false)
  private String name;
  @OneToMany(mappedBy = "category")
  private List<CategoryStore> categoryStores = new ArrayList<>();

  public Category(String name) {
    this.name = name;
  }
  public void changeName(String name) {
    this.name = name;
  }
}
