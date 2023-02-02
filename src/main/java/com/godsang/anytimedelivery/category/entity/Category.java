package com.godsang.anytimedelivery.category.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
  private String name;

  public Category(String name) {
    this.name = name;
  }

  public void changeName(String name) {
    this.name = name;
  }
}
