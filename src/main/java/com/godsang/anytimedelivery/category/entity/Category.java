package com.godsang.anytimedelivery.category.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
  @JsonBackReference
  private List<CategoryStore> categoryStores = new ArrayList<>();

  public Category(String name) {
    this.name = name;
  }

  public void changeName(String name) {
    this.name = name;
  }
}
